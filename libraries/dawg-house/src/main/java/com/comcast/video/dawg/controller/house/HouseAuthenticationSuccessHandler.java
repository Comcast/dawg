package com.comcast.video.dawg.controller.house;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.jwt.JwtAuthenticationSuccessHandler;

@Component
public class HouseAuthenticationSuccessHandler extends JwtAuthenticationSuccessHandler {
    
    @Autowired
    public HouseAuthenticationSuccessHandler(DawgJwtEncoder jwtEncoder) {
        super(jwtEncoder);
    }

    /**
     * On successful authentication, save a jwt into the cookies
     * 
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
                    throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);
        String targetUrl = "/" + authentication.getName();
        logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, "/login?logout");
    }
}
