package com.comcast.video.dawg.common.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {
    public static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    
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
        DawgCreds cookieCreds = getCookieCreds((HttpServletRequest) req, (HttpServletResponse) res);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((cookieCreds != null) && ((auth == null) || !auth.isAuthenticated())) {
            Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
            for (String role : cookieCreds.getRoles()) {
                auths.add(new SimpleGrantedAuthority(role));
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(cookieCreds.getUserName(), cookieCreds.getPassword(), auths);
            auth = this.authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        chain.doFilter(req, res);
        
    }
    
    /**
     * Gets the creds from the JWT stored in cookies or Authorization header.
     * If there is no jwt or it has expired and the user is still logged in
     * via their session, refresh the jwt.
     * @param req
     * @param res
     * @return
     */
    private DawgCreds getCookieCreds(HttpServletRequest req, HttpServletResponse res) {
        DawgCreds dawgCreds = null;
        String jwt = this.cookieUtils.extractJwt(req);
        if (jwt != null) {
            try {
                DawgJwt dawgJwt = this.encoder.decodeJwt(jwt);
                DawgCreds dawgUser = dawgJwt.getCreds();
                if (dawgUser != null) {
                    if (StringUtils.isEmpty(dawgUser.getUserName()) || StringUtils.isEmpty(dawgUser.getPassword())) {
                        LOGGER.warn("No username and password found in jwt");
                    } else {
                        dawgCreds = dawgUser;
                    }
                }
            } catch (Exception e) {
                LOGGER.debug("Invalid jwt '" + jwt + "'", e);
            }
        }
        if (dawgCreds == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if ((authentication != null) && (authentication.isAuthenticated())) {
                /** If we don't have a jwt or it is expired, we should refresh the jwt */
                LOGGER.debug("Refreshing jwt for '" + authentication.getName() + "'");
                dawgCreds = cookieUtils.toDawgCreds(authentication);
                jwt = this.encoder.createUserJWT(dawgCreds);
                res.addCookie(cookieUtils.createCookie(jwt, -1, req));
                cookieUtils.saveJwtInSession(req, jwt);
            }
        }
        return dawgCreds;
    }
    
}
