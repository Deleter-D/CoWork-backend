package org.only.cowork.service.Impl;

import org.only.cowork.entity.User;
import org.only.cowork.repository.UserRepository;
import org.only.cowork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of user-related service interfaces.
 *
 * @author WangYanpeng
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Create and save a User.
     *
     * @param user The user that waiting for save.
     * @return the user that was created and saved.
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Determines whether a user exists
     *
     * @param phoneNumber The phone number of the user to be determined.
     * @return a boolean value.
     */
    @Override
    public Boolean isUserNotExist(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber) == null ? true : false;
    }
}
