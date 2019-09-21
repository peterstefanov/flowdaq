package com.flowdaq.app.security.jwt;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.stereotype.Service;

import com.flowdaq.app.security.authentication.TokenAuthenticationService;
import com.flowdaq.app.service.date.DateService;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public final class JwtTokenService implements Clock, TokenService {

	private static final String DOT = ".";
	private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

	DateService dates;
	String issuer;
	int expirationSec;
	int clockSkewSec;
	String secretKey;

	public JwtTokenService(final DateService dates, @Value("${jwt.issuer:flowdaq}") final String issuer,
			@Value("${jwt.expiration-sec:100}") final int expirationSec,
			@Value("${jwt.clock-skew-sec:3000}") final int clockSkewSec,
			@Value("${jwt.secret:some_secret}") final String secret) {
		super();
		this.dates = requireNonNull(dates);
		this.issuer = requireNonNull(issuer);
		this.expirationSec = requireNonNull(expirationSec);
		this.clockSkewSec = requireNonNull(clockSkewSec);
		this.secretKey = BASE64.encode(requireNonNull(secret));
	}

	@Override
	public String permanent(final Map<String, String> attributes) {
		return newToken(attributes, 0);
	}

	@Override
	public String expiring(final Map<String, String> attributes) {
		return newToken(attributes, expirationSec);
	}

	private String newToken(final Map<String, String> attributes, final int expiresInSec) {
		final DateTime now = dates.now();
		final Claims claims = Jwts.claims().setIssuer(issuer).setIssuedAt(now.toDate());

		if (expiresInSec > 0) {
			final DateTime expiresAt = now.plusSeconds(expiresInSec);
			claims.setExpiration(expiresAt.toDate());
		}
		claims.putAll(attributes);

		return Jwts.builder().setClaims(claims).signWith(HS256, secretKey).compressWith(COMPRESSION_CODEC).compact();
	}

	@Override
	public Map<String, String> verify(final String token) {
		log.info("Verifing token:" + token);
		final JwtParser parser = Jwts.parser().requireIssuer(issuer).setClock(this)
				.setAllowedClockSkewSeconds(clockSkewSec).setSigningKey(secretKey);
		return parseClaims(() -> parser.parseClaimsJws(token).getBody());
	}

	@Override
	public Map<String, String> untrusted(final String token) {
		final JwtParser parser = Jwts.parser().requireIssuer(issuer).setClock(this)
				.setAllowedClockSkewSeconds(clockSkewSec);

		// https://github.com/jwtk/jjwt/issues/135
		final String withoutSignature = substringBeforeLast(token, DOT) + DOT;
		return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
	}

	private static Map<String, String> parseClaims(final Supplier<Claims> toClaims) {
		try {
			final Claims claims = toClaims.get();
			final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
			for (final Map.Entry<String, Object> e : claims.entrySet()) {
				builder.put(e.getKey(), String.valueOf(e.getValue()));
			}
			return builder.build();
		} catch (final IllegalArgumentException | JwtException e) {
			return ImmutableMap.of();
		}
	}

	@Override
	public Date now() {
		final DateTime now = dates.now();
		return now.toDate();
	}
}
