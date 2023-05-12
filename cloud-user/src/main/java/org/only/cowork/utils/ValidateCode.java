package org.only.cowork.utils;


import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * Validation code entity.
 *
 * @author WangYanpeng
 */
@Component
@Data
@ToString
public class ValidateCode {
    private String code;

    private LocalDateTime expirationTime;

    public ValidateCode() {
    }

    /**
     * Randomly take six numbers from the string "0123456789" to form a string as a verification code.
     *
     * @param expirationTime: an integer in seconds.
     */
    public ValidateCode(int expirationTime) {
        String sources = "0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            stringBuffer.append(sources.charAt(random.nextInt(9)));
        }

        this.code = stringBuffer.toString();
        this.expirationTime = LocalDateTime.now().plusSeconds(expirationTime);
    }
}
