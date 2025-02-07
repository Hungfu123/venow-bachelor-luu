package org.venow.backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = getUserIdFromUserDetails(userDetails);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String frontendBaeUrl = "http://localhost:5173";

        String redirectUrl;
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZER"))) {
            redirectUrl = frontendBaeUrl+"/organizer/" + userId + "/events";
        } else {
            redirectUrl = frontendBaeUrl+"/exhibitor/" + userId + "/events";
        }

        response.sendRedirect(redirectUrl);
    }

    private String getUserIdFromUserDetails(UserDetails userDetails) {
        return ((CustomUserDetails) userDetails).getUserId().toString();
    }
}
