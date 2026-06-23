package com.noskcire.movies.infrastructure.security.jwt;

import com.noskcire.movies.domain.enums.TokenType;
import com.noskcire.movies.domain.model.Token;
import com.noskcire.movies.infrastructure.adapter.output.persistence.repository.TokenRepository;
import com.noskcire.movies.infrastructure.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader =
                request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(
                    request,
                    response
            );
            return;
        }

        jwt = authHeader.substring(7);

        Token token =
                tokenRepository.findByTokenAndRevokedFalse(
                        jwt
                ).orElse(null);

        if (token == null
                || token.getType()
        != TokenType.ACCESS) {
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("""
                {
                    "success": false,
                    "message": "Token inválido o sesión finalizada",
                    "data": null
                }
                """);
            return;
        }

        username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder
                .getContext()
                .getAuthentication()
                == null
        ) {
            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(
                                    username
                            );
            if (
                    userDetails.isEnabled()
                    && jwtService.IsTokenValid(
                        jwt,
                        userDetails
                )
            ) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(
                request,
                response
        );
    }
}
