package org.only.cowork.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.only.cowork.security.CustomUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token utility class.
 *
 * @author WangYanpeng
 */
@Component
@Slf4j
public class TokenUtils {
    // The expiration time is 12 hours.
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;

    private static final String TOKEN_SECRET = "@7n^ls!i";


    public static String generateToken(CustomUserDetails userDetails) {
        try {
            Assert.notNull(userDetails, "UserDetails object is null.");
        } catch (IllegalArgumentException e) {
            throw e;
        }

        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

        Map<String, Object> header = new HashMap<>(2);
        header.put("Type", "Jwt");
        header.put("alg", "HMAC256");

        return JWT.create()
                .withHeader(header)
                .withClaim("id", userDetails.getUser().getId())
                .withClaim("phoneNumber", userDetails.getPhoneNumber())
                .withExpiresAt(expireTime)
                .sign(algorithm);
    }

    public static Map<String, Object> verifyToken(String token) {
        Map<String, Object> result = new HashMap<>();

        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);

            Integer id = decodedJWT.getClaim("id").asInt();
            String phoneNumber = decodedJWT.getClaim("phoneNumber").asString();

            result.put("id", id);
            result.put("phoneNumber", phoneNumber);
        } catch (TokenExpiredException e) {
            log.warn("The token is expired.");
            throw e;
        }

        return result;
    }
}
