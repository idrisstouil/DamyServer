package com.damy.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.damy.app.services.UserService;


@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	  private final UserService userDetailsService;
	  private final BCryptPasswordEncoder bCryptPasswordEncoder;
	  
	  UserService userService;
	  @Autowired
	  public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,UserService userService) {
	        this.userDetailsService = userDetailsService;
	        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	        this.userService=userService;
	    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		

		http
		    .cors().and()
		    .csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
			.permitAll()
			.antMatchers(HttpMethod.POST, "/users/login")
			.permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilter(getAuthenticationFilter())
			.addFilter(new AuthorizationFilter(authenticationManager()))
			.sessionManagement()
		    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	
	protected AuthenticationFilter getAuthenticationFilter() throws Exception {
	    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(),userService);
	    filter.setFilterProcessesUrl("/users/login");
	    return filter;
	}
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
}
