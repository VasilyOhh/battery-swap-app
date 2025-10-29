package com.evswap.evswapstation.config;

import com.evswap.evswapstation.entity.User;
import com.evswap.evswapstation.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Danh sách paths không cần JWT
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/",
            "/swagger-ui/",
            "/v3/api-docs/",
            "/swagger-resources/",
            "/webjars/"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // Skip cho OPTIONS request (preflight)
        if ("OPTIONS".equalsIgnoreCase(method)) {
            System.out.println("⏭️ Skipping JWT filter for OPTIONS request: " + path);
            return true;
        }
        
        boolean shouldSkip = EXCLUDED_PATHS.stream()
                .anyMatch(path::startsWith);

        if (shouldSkip) {
            System.out.println("⏭️ Skipping JWT filter for: " + path + " (method: " + method + ")");
        }

        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("🔐 JWT Filter processing: " + request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Kiểm tra nếu userDetails là CustomUserDetails
                if (userDetails instanceof com.evswap.evswapstation.security.CustomUserDetails) {
                    com.evswap.evswapstation.security.CustomUserDetails customUserDetails =
                            (com.evswap.evswapstation.security.CustomUserDetails) userDetails;
                    User user = customUserDetails.getUser();

                    if (jwtService.isTokenValid(jwt, user)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        System.out.println("✅ JWT validated for: " + username);
                    } else {
                        System.out.println("❌ Invalid JWT token");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ JWT error: " + e.getMessage());
            // KHÔNG throw exception ở đây
        }

        filterChain.doFilter(request, response);
    }
}