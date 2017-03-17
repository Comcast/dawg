package com.comcast.video.dawg.common.security.jwt;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.google.common.net.InternetDomainName;

/**
 * Utils around jwt cookies
 * @author Kevin Pearson
 *
 */
public class DawgCookieUtils {
    public static final String COOKIE_NAME = "dawt";
    
    /**
     * Creates a cookie with the given value that is scoped under the private domain of the url that was requested
     * @param value
     * @param maxAge
     * @param request
     * @return
     */
    public Cookie createCookie(String value, int maxAge, HttpServletRequest request) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setSecure(false);
        InternetDomainName dn = InternetDomainName.from(request.getServerName());
        String privateDomain = dn.isUnderPublicSuffix() ? dn.topPrivateDomain().toString() : request.getServerName();
        cookie.setDomain(privateDomain);
        return cookie;
    }
    
    public org.apache.http.cookie.Cookie createApacheCookie(String value, int maxAge, HttpServletRequest request) {
        return servletCookieToApacheCookie(createCookie(value, maxAge, request));
    }
    
    public Cookie getDawtCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    /**
     * Gets the jwt string out of either the session, cookie or authorization header
     * @param request
     * @return
     */
    public String extractJwt(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            String jwt = (String) session.getAttribute(DawgCookieUtils.COOKIE_NAME);
            if (jwt != null) {
                return jwt;
            }
        }
        Cookie dawt = getDawtCookie(request);
        String jwt = dawt == null ? null : dawt.getValue();
        if (jwt == null) {
            String authHeader = request.getHeader("Authorization");
            if ((authHeader != null) && (authHeader.startsWith("Bearer"))) {
                jwt = new String(Base64.decodeBase64(authHeader.substring("Bearer ".length())));
            }
        }
        return jwt;
    }
    
    public org.apache.http.cookie.Cookie servletCookieToApacheCookie(Cookie cookie) {
        BasicClientCookie apacheCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
        apacheCookie.setComment(cookie.getComment());
        apacheCookie.setDomain(cookie.getDomain());
        apacheCookie.setAttribute(ClientCookie.DOMAIN_ATTR, cookie.getDomain());
        apacheCookie.setPath(cookie.getPath());
        apacheCookie.setSecure(cookie.getSecure());
        apacheCookie.setVersion(cookie.getVersion());
        
        return apacheCookie;
    }
    
    public DawgCreds toDawgCreds(Authentication authentication) {
        Set<String> roles = new HashSet<String>();
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            roles.add(auth.getAuthority());
        }
        String pass = authentication.getCredentials() == null ? null : authentication.getCredentials().toString();
        return new DawgCreds(authentication.getName(), pass, roles);
    }
    
    public void saveJwtInSession(HttpServletRequest req, String jwt) {
        HttpSession session = req.getSession();
        if (session != null) {
            session.setAttribute(COOKIE_NAME, jwt);
        }
    }
}
