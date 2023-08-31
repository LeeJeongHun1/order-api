package com.order.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    // Expiration Time
    public final long MINUTE = 1000 * 60;
    public final long HOUR = 60 * MINUTE;
    public final long DAY = 24 * HOUR;
    public final long MONTH = 30 * DAY;

    public final long AT_EXP_TIME = 1 * DAY;
    public final long RT_EXP_TIME = 1 * MONTH;

    // Claim
    public final String CLAIM_ID = "id";
    public final String CLAIM_ROLE = "role";
    @Value("${jwt.access-token.secret-key}")
    private String SECRET_KEY;

    /**
     * SecurityContextHolder 에 저장된 accessToken 반환.
     * @return
     */
    public String getAccessToken() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    /**
     * 기본적으로 로그인이 필수인 Endpoint 에 대한 요청
     * 로그인이 되어 있으므로 SecurityContextHolder 에 access token 저장되어 있음
     */
    public String getEmail() {
        String accessToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        DecodedJWT decodedJWT = verifyToken(accessToken);
        return decodedJWT.getSubject();
    }

    public Integer getId() {
        String accessToken = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        DecodedJWT decodedJWT = verifyToken(accessToken);
        return decodedJWT.getClaim(CLAIM_ID).asInt();
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(HMAC256(SECRET_KEY)).build();
        return verifier.verify(token);
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + AT_EXP_TIME);

        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(HMAC256(SECRET_KEY));
    }

    public String createRefreshToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + RT_EXP_TIME);

        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(HMAC256(SECRET_KEY));
    }

}
