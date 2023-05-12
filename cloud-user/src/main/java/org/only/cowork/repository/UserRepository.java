package org.only.cowork.repository;

import org.only.cowork.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User-related Repository interface.
 *
 * @author WangYanpeng
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);
}
