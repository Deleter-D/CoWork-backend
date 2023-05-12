package org.only.cowork.test.test_repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.only.cowork.entity.User;
import org.only.cowork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        Date currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        User user = User.builder()
                .username("goldpancake")
                .phoneNumber("18735855248")
                .password("123456")
                .isAccountNonLocked(false)
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();

        User save = userRepository.save(user);
        log.info(save.toString());
    }

    @Test
    public void fingByPhoneNumberTest() {
        User user = userRepository.findByPhoneNumber("18735855248");
        log.info(user.toString());
    }
}
