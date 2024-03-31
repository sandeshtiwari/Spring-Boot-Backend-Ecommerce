package com.backend.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import com.backend.filter.JwtAuthenticationFilter;
import com.backend.service.impl.UserDetailsServiceImp;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserDetailsServiceImp userDetailsServiceImp;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private CustomLogoutHandler logoutHandler;

	public SecurityConfig(UserDetailsServiceImp userDetailsServiceImp,
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.userDetailsServiceImp = userDetailsServiceImp;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowCredentials(true);
					config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
					config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
					config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-CSRF-Token"));
					config.setExposedHeaders(Arrays.asList("Custom-Header1", "Custom-Header2"));
					return config;
				}))
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						req -> req.requestMatchers("/api/login/**", "/api/register/**", "/api/products/**")
								.permitAll()
								.requestMatchers("/api/order/**", "/api/users/**").hasAnyAuthority("USER")
								.requestMatchers("/api/admin/**").hasAnyAuthority("ADMIN")
								.anyRequest()
								.authenticated())
				.userDetailsService(userDetailsServiceImp)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exceptions -> exceptions
						.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(l -> l.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "POST"))
						// l.logoutUrl("/logout")
						.addLogoutHandler(logoutHandler)
						// .logoutSuccessHandler((request, response, authentication) ->
						// SecurityContextHolder.clearContext()))
						.logoutSuccessHandler((request, response, authentication) -> {
							System.out.println("Logout Success Handler " + request.getHeader("Authorization"));
							if (request.getHeader("Authorization") == null) {
								response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
								response.setContentType(MediaType.APPLICATION_JSON_VALUE);

								// Custom message for failed logout
								String customMessage = "{\"message\": \"Logout failed. Not Authenticated/Authorized\"}";
								response.getWriter().write(customMessage);
								return;
							}
							SecurityContextHolder.clearContext();
							response.setStatus(HttpServletResponse.SC_OK);
							response.setContentType(MediaType.APPLICATION_JSON_VALUE);

							// Custom message for logout
							String customMessage = "{\"message\": \"Logout successful\"}";
							response.getWriter().write(customMessage);
						}))
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
