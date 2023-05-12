package org.only.cowork.test.test_service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.only.cowork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void isUserExistTest() {
        log.info(userService.isUserNotExist("18735855243").toString());
    }
}
