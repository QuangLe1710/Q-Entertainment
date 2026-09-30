package com.app.Q_Entertainment.Config;

import com.app.Q_Entertainment.Util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                String username = jwtTokenUtil.getUsername(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Claims claims = jwtTokenUtil.getClaims(jwtToken);

                if (!jwtTokenUtil.isTokenExpired(jwtToken)
                        && userDetails.getUsername().equals(claims.getSubject())) {
                    Authentication authentication = jwtTokenUtil.getAuthentication(jwtToken, userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (RuntimeException ignored) {
                // Invalid tokens remain unauthenticated and are handled by Spring Security.
            }
        }

        filterChain.doFilter(request, response);
    }
}
