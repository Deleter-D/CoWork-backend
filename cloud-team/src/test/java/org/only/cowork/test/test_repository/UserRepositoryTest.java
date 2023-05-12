package org.only.cowork.test.test_repository;

import org.junit.jupiter.api.Test;
import org.only.cowork.entity.User;
import org.only.cowork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByPhoneNumberTest() {
        User user = userRepository.findByPhoneNumber("18735855248");
        System.out.println(user);
    }
}
