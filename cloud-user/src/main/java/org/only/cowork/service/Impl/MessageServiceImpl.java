package org.only.cowork.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.only.cowork.utils.ValidateCode;
import org.only.cowork.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * This class is an implementation of the SMS service class.
 *
 * @author WangYanpeng
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    /**
     * Create an instance of the ValidateCode class and send the verification code to the user.
     *
     * @return A validateCode entity class, which contains the verification code and expiration time
     */
    @Override
    public ValidateCode getAuthCode() {
        int expirationTime = 300;

        ValidateCode validateCode = new ValidateCode(expirationTime);

        // TODO send authCode to users' phone.
        log.info("【CoWork】您的验证码为" + validateCode.getCode() + "，有效期" + expirationTime / 60 + "分钟，请妥善保存，切勿传给他人。");

        return validateCode;
    }
}
