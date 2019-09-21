package com.flowdaq.app.config.filter;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowdaq.app.model.User;
import com.flowdaq.app.model.response.Response;
import com.flowdaq.app.model.response.Response.ResponseStatusEnum;
import com.flowdaq.app.security.authentication.UserAuthenticationService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenValidationFilter extends GenericFilterBean {

	private static final String BEARER = "Bearer";

	private UserAuthenticationService authentication;

	public TokenValidationFilter(UserAuthenticationService authentication) {
		this.authentication = authentication;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		try {
			final String param = ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getParameter("t"));
			final String token = ofNullable(param).map(value -> removeStart(value, BEARER)).map(String::trim)
					.orElseThrow(() -> new BadCredentialsException("Missing Authentication Token"));

			if (StringUtils.isNotBlank(token)) {

				Optional<User> user = authentication.findByToken(token);

				UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(user.get(),
						user.get().getPassword(), user.get().getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authtoken);
			}
			filterChain.doFilter(req, res);
		} catch (Exception e) {
			log.error(e.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			if (e instanceof ExpiredJwtException) {
				Response resp = new Response();
				resp.setOperationStatus(ResponseStatusEnum.TOKEN_EXIPRED);
				resp.setMessage(e.getMessage());

				ObjectMapper ow = new ObjectMapper();

				String jsonRespString = ow.writeValueAsString(resp);
				response.getWriter().write(jsonRespString);
				response.getWriter().flush();
				response.getWriter().close();
			}
		}
	}
}