package org.only.cowork.test.test_utils;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.only.cowork.entity.User;
import org.only.cowork.security.CustomUserDetails;
import org.only.cowork.utils.TokenUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@SpringBootTest
@Slf4j
public class TokenUtilsTest {

    @Test
    public void generateTokenTest() {
        Date currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        User user = User.builder()
                .username("goldpancake")
                .phoneNumber("18735855248")
                .password("123456")
                .isAccountNonLocked(true)
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();

        CustomUserDetails userDetails = CustomUserDetails.builder()
                .user(user)
                .build();

        log.info(TokenUtils.generateToken(userDetails));
    }

    @Test
    public void verifyTokenTest() {
        try {
            Map<String, Object> result = TokenUtils.verifyToken("eyJUeXBlIjoiSnd0IiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJwaG9uZU51bWJlciI6IjE4NzM1ODU1MjQ4IiwiaWQiOjEsImV4cCI6MTY4MjE0ODIzOH0.oxfFjlDXeJm_PAJzQV4d42T3-fGqGQfLjfsdEyLIEvQ");

            log.info(String.valueOf(result.get("id")));
            log.info(String.valueOf(result.get("phoneNumber")));
        } catch (TokenExpiredException e) {
            log.warn("Token has been expired.");
        }
    }
}
