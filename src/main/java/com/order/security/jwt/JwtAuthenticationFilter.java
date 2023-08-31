package com.order.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.net.HttpHeaders;
import com.order.exception.UnauthorizedException;
import com.order.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 토큰 유효성 검증
 * OncePerRequestFilter : http 요청별 한 번만 실행되도록 보장되는 필터
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final String TOKEN_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;

    // 인증에서 제외할 url
    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/",
                            "/webjars/**",
                            "/h2-console/**",
                            "/swagger-ui/**",
                            "/api-docs/**",
                            "/static/**",
                            "/favicon.ico"
                    ));
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        //인증 토큰이 존재하고, 그 값이 Bearer 토큰이면, 토큰을 decode 한다.
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            String accessToken = authorizationHeader.substring(TOKEN_PREFIX.length());

            try {
                DecodedJWT decodedJWT = jwtProvider.verifyToken(accessToken);
                String username = decodedJWT.getSubject();
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthxToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                accessToken,
                                userDetails.getAuthorities()
                        );

                usernamePasswordAuthxToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(usernamePasswordAuthxToken);

            } catch (BadCredentialsException e) {
                throw new UnauthorizedException("Token is Invalid");
            } catch (JWTVerificationException e) {
                throw new UnauthorizedException("Token is Expired");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return EXCLUDE_URL.stream().anyMatch(exclude -> pathMatcher.match(exclude, request.getServletPath()));
    }
}

