package fr.formation.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration of the security
 * 
 *
 */
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	static final String ROLE_ADMIN="ADMIN";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable().authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/health/").permitAll()
			.antMatchers(HttpMethod.PUT,"/users/").permitAll()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.anyRequest().authenticated()
				.and()	
			.logout().permitAll()
				.and()
			// We filter the api/login requests
	        .addFilterBefore(new JwtLoginFilter("/login", authenticationManager()),
	                		UsernamePasswordAuthenticationFilter.class)
	        // And filter other requests to check the presence of JWT in header
	        .addFilterBefore(new JwtAuthenticationFilter(),
	                		UsernamePasswordAuthenticationFilter.class);
		// @formatter:on
	}

}