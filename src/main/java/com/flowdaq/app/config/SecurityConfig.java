package com.flowdaq.app.config;

import static lombok.AccessLevel.PRIVATE;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.flowdaq.app.config.filter.CorsFilter;
import com.flowdaq.app.config.filter.TokenValidationFilter;
import com.flowdaq.app.security.authentication.UserAuthenticationService;
import com.flowdaq.app.service.user.UserServiceImpl;

import lombok.experimental.FieldDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final RequestMatcher url = new AntPathRequestMatcher("/");
	private static final RequestMatcher publicUrl = new AntPathRequestMatcher("/public/**");
	private static final RequestMatcher resourcesUrl = new AntPathRequestMatcher("/resources/**");
	private static final RequestMatcher staticrl = new AntPathRequestMatcher("/static/**");
	private static final RequestMatcher webUrl = new AntPathRequestMatcher("/webui/**");
	private static final RequestMatcher htmlUrl = new AntPathRequestMatcher("/*.html");
	private static final RequestMatcher htmlDeepUrl = new AntPathRequestMatcher("/**/*.html");
	private static final RequestMatcher jsUrl = new AntPathRequestMatcher("/**/*.js");
	private static final RequestMatcher icoUrl = new AntPathRequestMatcher("/**/*.ico");
	private static final RequestMatcher cssUrl = new AntPathRequestMatcher("/**/*.css");
	private static final RequestMatcher scssUrl = new AntPathRequestMatcher("/**/*.scss");

	private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(url, publicUrl, resourcesUrl, staticrl,
			webUrl, htmlUrl, htmlDeepUrl, jsUrl, icoUrl, cssUrl, scssUrl,
			new AntPathRequestMatcher("/configuration/**"), new AntPathRequestMatcher("/swagger-ui/**"),
			new AntPathRequestMatcher("/swagger-resources/**"), new AntPathRequestMatcher("/api-docs"),
			new AntPathRequestMatcher("/api-docs/**"), new AntPathRequestMatcher("/v2/api-docs/**"),
			new AntPathRequestMatcher("/**/*.png"), new AntPathRequestMatcher("/**/*.jpg"),
			new AntPathRequestMatcher("/**/*.gif"), new AntPathRequestMatcher("/**/*.svg"),
			new AntPathRequestMatcher("/**/*.ttf"), new AntPathRequestMatcher("/**/*.woff"),
			new AntPathRequestMatcher("/**/*.otf"));

	private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

	private final UserServiceImpl usersService;

	private final UserAuthenticationService authentication;

	public SecurityConfig(UserServiceImpl usersService,
			@Qualifier("tokenAuthenticationService") final UserAuthenticationService authentication) {
		super();
		this.usersService = usersService;
		this.authentication = authentication;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		usersService.setPasswordEncoder(passwordEncoder());
		auth.userDetailsService(usersService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(final WebSecurity web) {
		web.ignoring().requestMatchers(PUBLIC_URLS);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.anonymous().and().csrf().disable().addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
				.addFilterBefore(new TokenValidationFilter(authentication), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().anyRequest().authenticated();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder(4);
		return encoder;
	}
}
