package com.comcast.video.dawg.controller.pound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.comcast.video.dawg.common.security.DawgSpringLdapConfigurer;
import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private DawgPoundConfiguration config;
    
    @Autowired
    private DawgJwtEncoder jwtEncoder;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

	    JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtEncoder, authenticationManager());
		http
		    .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers("/health/**").permitAll()
			.antMatchers("/admin/**").hasAnyRole("ADMIN")
			.antMatchers("/**").hasAnyRole("ADMIN", "POUND");
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        DawgSpringLdapConfigurer.configureGlobal(auth, config.getLdapAuthConfig(), passwordEncoder);
    }
}