package com.comcast.video.dawg.common.security.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;

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
    
    /**
     * Gets the jwt string out of either the cookie or authorization header
     * @param request
     * @return
     */
    public String extractJwt(HttpServletRequest request) {
        String jwt = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COOKIE_NAME)) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }
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
}
