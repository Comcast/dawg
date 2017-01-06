package com.comcast.video.dawg.show;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.comcast.video.dawg.common.security.jwt.DawgJwtEncoder;
import com.comcast.video.dawg.common.security.jwt.JwtAuthenticationSuccessHandler;

@Component
public class ShowAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements LogoutSuccessHandler {
    
    private JwtAuthenticationSuccessHandler jwtSuccess;
    
    @Autowired
    public ShowAuthenticationSuccessHandler(DawgJwtEncoder jwtEncoder) {
        this.jwtSuccess = new JwtAuthenticationSuccessHandler(jwtEncoder);
    }

    /**
     * On successful authentication, save a jwt into the cookies and then redirect to 
     * which ever site the user was planning on going to before logging in
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
                    throws ServletException, IOException {
        this.jwtSuccess.onAuthenticationSuccess(request, response, authentication);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        this.jwtSuccess.onLogoutSuccess(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, "/login?logout");
    }
}
