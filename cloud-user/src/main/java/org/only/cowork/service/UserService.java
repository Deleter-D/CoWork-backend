package org.only.cowork.service;

import org.only.cowork.entity.User;

/**
 * User-related service interfaces.
 *
 * @author WangYanpeng
 */
public interface UserService {
    User save(User user);

    Boolean isUserNotExist(String phoneNumber);
}
