package com.example.Aivra_Backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("JWT FILTER REACHED - AuthHeader: " + authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        
        // Handle case where "Bearer" appears twice (e.g., "Bearer Bearer token")
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);

        }
        

        try {
            Jwt jwt = jwtDecoder.decode(token);
            System.out.println("JWT decoded successfully");

            String subject = jwt.getSubject();
            List<String> roles = jwt.getClaimAsStringList("roles");
            System.out.println("Subject: " + subject + ", Roles: " + roles);

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(subject, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication set in SecurityContext");

        } catch (JwtValidationException e) {
            logger.error("JWT validation failed: " + e.getMessage());
            System.out.println("JWT validation failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("JWT decoding error: " + e.getMessage());
            System.out.println("JWT decoding error: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
