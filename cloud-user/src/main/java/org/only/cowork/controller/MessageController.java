package org.only.cowork.controller;

import org.only.cowork.utils.ResponseTemplate;
import org.only.cowork.utils.ValidateCode;
import org.only.cowork.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The Controller of the SMS service, which provides a series of SMS related interfaces.
 *
 * @author WangYanpeng
 */
@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private ResponseTemplate<String> responseTemplate;

    /**
     * The front-end obtains the SMS verification code through this request.
     *
     * @return validation code.
     */
    @GetMapping("/message")
    public ResponseTemplate<String> getAuthCode(@RequestParam(required = true) String phoneNumber, HttpServletRequest request) {
        ValidateCode validateCode = messageService.getAuthCode();

        // Store the verification code in session.
        HttpSession session = request.getSession();
        session.removeAttribute(phoneNumber);
        session.setAttribute(phoneNumber, validateCode);

        responseTemplate.setCode(1001);
        responseTemplate.setData("Get the verification code successfully.");
        return responseTemplate;
    }
}
