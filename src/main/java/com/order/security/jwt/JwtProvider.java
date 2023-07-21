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
//    private final CustomUserDetailsService customUserDetailsService;

    // Expiration Time
    public final long MINUTE = 1000 * 60;
    public final long HOUR = 60 * MINUTE;
    public final long DAY = 24 * HOUR;
    public final long MONTH = 30 * DAY;

//    public final long AT_EXP_TIME = 5 * MINUTE;

    public final long AT_EXP_TIME = 1 * DAY;
    public final long RT_EXP_TIME = 1 * MONTH;

    // Refresh Token 갱신 주기(Day)
    public final long TOKEN_REFRESH_DAYS = 30;

    // Secret
    //public final String JWT_SECRET;

    // Header
    public final String AT_HEADER = "accessToken";
    public final String RT_HEADER = "refreshToken";
    public final String TOKEN_HEADER_PREFIX = "Bearer ";

    // Claim
    public final String CLAIM_ID = "id";
    public final String CLAIM_ROLE = "role";
    public final String CLAIM_U = "role";
    public final String CONFIRM_TYPE = "confirm_type";
    @Value("${jwt.access-token.secret-key}")
    private String SECRET_KEY;

    /**
     * SecurityContextHolder 에 저장된 accessToken 반환.
     * @return
     */
    public String getAccessToken() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
////            throw new CustomException(TOKEN_NOT_EXIST, TOKEN_NOT_EXIST.name());
//        }
//        return authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
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

    public DecodedJWT GetDecodedJWT(String token) {
        return JWT.decode(token);
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(HMAC256(SECRET_KEY)).build();
        return verifier.verify(token);
    }

    public Instant getExpireDate(String token) {
        JWTVerifier verifier = JWT.require(HMAC256(SECRET_KEY)).build();
        return verifier.verify(token).getExpiresAtAsInstant();
    }

//    public String createAccessToken(String email, String roleType, Integer id) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + AT_EXP_TIME);
//
//        return JWT.create()
//                .withSubject(email)
//                .withIssuedAt(now)
//                .withExpiresAt(validity)
//                .withClaim(CLAIM_ROLE, roleType)
//                .withClaim(CLAIM_ID, id)
//                .sign(HMAC256(SECRET_KEY));
//    }

//    public String createRefreshToken(String email) {
//        return JWT.create()
//                .withSubject(email)
//                .withExpiresAt(new Date(System.currentTimeMillis() + RT_EXP_TIME))
//                .sign(HMAC256(SECRET_KEY));
//    }

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

//    public Authentication getAuthentication(String accessToken) {
//        DecodedJWT decodedJWT = verifyToken(accessToken);
//
//        UserDetails userDetails = customUserDetailsService.loadUserByUsername(decodedJWT.getSubject());
//        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
//    }

//    public String createConfirmToken(ConfirmationToken confirmationToken) {
//        return JWT.create()
//                .withIssuedAt(convertToDateViaInstant(confirmationToken.getCreateDate()))
//                .withExpiresAt(convertToDateViaInstant(confirmationToken.getExpirationDate()))
//                .withClaim(CLAIM_ID, confirmationToken.getUserId())
//                .withClaim(CONFIRM_TYPE, confirmationToken.getConfirmType().toString())
//                .sign(HMAC256(SECRET_KEY));
//    }

    private Date convertToDateViaInstant(Instant dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

//    // Refresh Token의 남은 만료 일자(day) 계산
//    public Long calculateRefreshExpiredDays(DecodedJWT decodedJWT) {
//        long expTime = decodedJWT.getClaim("exp").asLong() * 1000;
//        return (expTime - System.currentTimeMillis()) / DAY;
//    }

}
