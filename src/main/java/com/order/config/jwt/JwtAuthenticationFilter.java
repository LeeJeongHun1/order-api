package com.order.config.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.net.HttpHeaders;
import com.order.config.CustomUserDetailsService;
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
                            "/favicon.ico",
//                            "/v2/auth/**",
                            "/v2/health"
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

//                BearerTokenAuthenticationToken
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
//                responseWriter(response,  new UnauthorizedException("AUTH.TOKEN_INVALID"));
                return;
            } catch (JWTVerificationException e) {
//                responseWriter(response,  new UnauthorizedException("AUTH.TOKEN_EXPIRED"));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Contoller내에서 인증유저와 익명유저 동시에 loginUser.userId를 사용할 수 있도록 처리
     * (기존에는 익명유저가 loginUser가 null이라 loginUser.userId를 사용못함)
     * @return
     */
//    private Authentication generateAnonymousAuthx() {
//        UserDetails anonymousUser = CustomUser.Anonymous().build();
//        return new AnonymousAuthenticationToken(
//                        anonymousUser.getUsername(),
//                        anonymousUser,
//                        anonymousUser.getAuthorities()
//                );
//    }

    /**
     * jwt 토큰 유효성 검증 실패 시 response 응답 메소드.
//     * @param response
//     * @param customException
     * @throws IOException
     */
//    private void responseWriter(HttpServletResponse response, CustomException customException) throws IOException {
//        response.setContentType(APPLICATION_JSON_VALUE);
//        response.setCharacterEncoding("utf-8");
//        response.setStatus(customException.getHttpStatus().value());
//
//        ExceptionResponse errorResponse = new ExceptionResponse();
//        errorResponse.setErrorCode(customException.getErrorCode());
//        errorResponse.setErrorMessage(messageManager.getMessage(customException.getErrorCode()));
//        errorResponse.setTimestamp(Instant.now());
//        new ObjectMapper().registerModule(new JavaTimeModule())
//                .writeValue(response.getWriter(), errorResponse);
//    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return EXCLUDE_URL.stream().anyMatch(exclude -> pathMatcher.match(exclude, request.getServletPath()));
    }
}
