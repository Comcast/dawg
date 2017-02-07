package com.comcast.video.dawg.common.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler implements LogoutSuccessHandler {
    
    protected DawgJwtEncoder jwtEncoder;
    protected DawgCookieUtils cookieUtils = new DawgCookieUtils();
    
    public JwtAuthenticationSuccessHandler(DawgJwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * On successful authentication, save a jwt into the cookies
     * 
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
                    throws ServletException, IOException {
        DawgCreds creds = cookieUtils.toDawgCreds(authentication);
        String jwt = jwtEncoder.createUserJWT(creds);
        response.addCookie(cookieUtils.createCookie(jwt, -1, request));
        cookieUtils.saveJwtInSession(request, jwt);
    }

    /**
     * When logging out, expire the cookie
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.addCookie(cookieUtils.createCookie(null, 0, request));
    }
}
