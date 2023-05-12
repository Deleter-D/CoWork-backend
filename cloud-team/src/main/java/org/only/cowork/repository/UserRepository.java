package org.only.cowork.repository;

import org.only.cowork.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User-related Repository interface.
 *
 * @author WangYanpeng
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByPhoneNumber(String phoneNumber);
}
