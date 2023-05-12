package org.only.cowork.security;

import lombok.extern.slf4j.Slf4j;
import org.only.cowork.entity.User;
import org.only.cowork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom UserDetailsService class.
 *
 * @author WangYanpeng
 */
@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {

        // find user in database.
        User user = userRepository.findByPhoneNumber(phoneNumber);
        try {
            Assert.notNull(user);
        } catch (IllegalArgumentException e) {
            log.warn("User not found.");
            throw e;
        }

        // Set all user roles to USER, and the detailed roles are resolved through other services.
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        grantedAuthorityList.add(grantedAuthority);

        return new CustomUserDetails(user);
    }
}
