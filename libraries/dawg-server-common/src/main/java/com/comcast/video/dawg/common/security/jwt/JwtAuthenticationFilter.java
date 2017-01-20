package com.comcast.video.dawg.common.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {
    public static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class);
    
    private DawgJwtEncoder encoder;
    private AuthenticationManager authenticationManager;
    private DawgCookieUtils cookieUtils = new DawgCookieUtils();
    
    public JwtAuthenticationFilter(DawgJwtEncoder encoder, AuthenticationManager authenticationManager) {
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Performs the security filter.
     * This will parse the jwt token either from a cookie or the Authorization header
     * The username, password, and roles will be obtained from the jwt and then
     * this account will be passed on to the authentication manager which
     * will then validate via LDAP.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String jwt = this.cookieUtils.extractJwt((HttpServletRequest) req);
        if (jwt != null) {
            DawgCreds dawgUser = null;
            try {
                DawgJwt dawgJwt = this.encoder.decodeJwt(jwt);
                dawgUser = dawgJwt.getCreds();
            } catch (Exception e) {
                LOGGER.warn("Invalid jwt '" + jwt + "'", e);
            }
            if (dawgUser != null) {
                Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
                for (String role : dawgUser.getRoles()) {
                    auths.add(new SimpleGrantedAuthority(role));
                }
                if (StringUtils.isEmpty(dawgUser.getUserName()) || StringUtils.isEmpty(dawgUser.getPassword())) {
                    LOGGER.warn("No username and password found in jwt");
                } else {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dawgUser.getUserName(), dawgUser.getPassword(), auths);
                    Authentication auth = this.authenticationManager.authenticate(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        chain.doFilter(req, res);
        
    }
    
}
