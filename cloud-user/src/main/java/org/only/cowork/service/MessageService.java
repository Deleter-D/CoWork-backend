package org.only.cowork.service;

import org.only.cowork.utils.ValidateCode;

/**
 * This class implements the SMS verification code service.
 * Six random numbers are used as the verification code for simulation.
 * Later, it can access the SMS verification code service of various cloud platforms.
 *
 * @author WangYanpeng
 */
public interface MessageService {

    /**
     * This method generates a verification code.
     *
     * @return A 6-digit string.
     */
    ValidateCode getAuthCode();
}
