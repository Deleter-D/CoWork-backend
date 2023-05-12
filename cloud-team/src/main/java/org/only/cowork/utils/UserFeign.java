package org.only.cowork.utils;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * The OpenFeign interface of user service.
 *
 * @author WangYanpeng
 */
@FeignClient(value = "service-user")
public interface UserFeign {

    @GetMapping("/verify")
    Map<String, Object> verifyUserByToken(@RequestParam String token);
}
