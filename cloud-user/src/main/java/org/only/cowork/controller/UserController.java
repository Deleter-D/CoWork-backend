package org.only.cowork.controller;


import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.only.cowork.entity.User;
import org.only.cowork.security.CustomUserDetails;
import org.only.cowork.security.CustomUserDetailsService;
import org.only.cowork.service.UserService;
import org.only.cowork.utils.ResponseTemplate;
import org.only.cowork.utils.TokenUtils;
import org.only.cowork.utils.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The Controller of the user service, which provides a series of user related interfaces.
 *
 * @author WangYanpeng
 */
@RestController
@Slf4j
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ResponseTemplate<String> stringResponse;

    @Autowired
    private ResponseTemplate<Map<String, String>> mapResponse;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * @param username     user's name, can be any nickname.
     * @param phoneNumber  user's phone number, used to bind user.
     * @param password     user's password
     * @param validateCode the verification code received by the user.
     * @param request      no need to pass the parameter.
     * @return a response template instantiated by the String type
     */
    @PostMapping("/register")
    public ResponseTemplate<String> register(@RequestParam String username,
                                             @RequestParam String phoneNumber,
                                             @RequestParam String password,
                                             @RequestParam String validateCode,
                                             HttpServletRequest request) {
        try {
            Assert.isTrue(userService.isUserNotExist(phoneNumber), "The phone number has been registered, please go to login.");
        } catch (IllegalArgumentException e) {
            stringResponse.setCode(1105);
            stringResponse.setData("The phone number has been registered, please go to login.");
            return stringResponse;
        }

        // Check that the verification code
        HttpSession session = request.getSession();
        ValidateCode trueValidateCode = (ValidateCode) session.getAttribute(phoneNumber);

        // Check whether the verification code exists.
        try {
            Assert.notNull(trueValidateCode, "The verification code has not been obtained yet.");
        } catch (IllegalArgumentException e) {
            stringResponse.setCode(1102);
            stringResponse.setData("The verification code has not been obtained yet.");
            return stringResponse;
        }

        // Check whether the verification code has expired.
        // If so, delete the verification code from the session.
        try {
            Assert.isTrue(trueValidateCode.getExpirationTime().isAfter(LocalDateTime.now()), "Verification code has expired.");
        } catch (IllegalArgumentException e) {
            stringResponse.setCode(1103);
            stringResponse.setData("Verification code has expired.");
            return stringResponse;
        } finally {
            session.removeAttribute(phoneNumber);
        }

        // Check whether the verification code entered by the user is consistent with the actual verification code.
        try {
            Assert.isTrue(validateCode.equals(trueValidateCode.getCode()), "Verification code error.");
        } catch (IllegalArgumentException e) {
            stringResponse.setCode(1104);
            stringResponse.setData("Verification code error.");
            return stringResponse;
        }

        // Get current local time and create the user entity and save it.
        Date currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        User user = User.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .password(password)
                .isAccountNonLocked(true)
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();

        userService.save(user);
        stringResponse.setCode(1101);
        stringResponse.setData("Verification code correct.");
        session.removeAttribute(phoneNumber);

        return stringResponse;
    }

    /**
     * @param phoneNumber user's phone number, used to bind user.
     * @param password    user's password
     * @return a response template instantiated by the String or Map.
     */
    @PostMapping("/login")
    public ResponseTemplate<String> login(@RequestParam String phoneNumber,
                                          @RequestParam String password) {
        try {
            // create UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(phoneNumber, password);

            // authentication
            Authentication authenticate = authenticationManager.authenticate(token);


            // save authentication information
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            // load UserDetails, we use users' phone number to load the UserDetails
            CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(phoneNumber);

            // generate custom token
            String customToken = TokenUtils.generateToken(userDetails);

            HashMap<String, String> responseMap = new HashMap<>();
            responseMap.put("token", customToken);
            responseMap.put("username", userDetails.getUsername());

            mapResponse.setCode(1201);
            mapResponse.setData(responseMap);

        } catch (InternalAuthenticationServiceException e) {
            stringResponse.setCode(1202);
            stringResponse.setData("Not yet registered, please register before login.");
        } catch (BadCredentialsException e) {
            stringResponse.setCode(1203);
            stringResponse.setData("Password is not correct.");
        }

        return stringResponse;
    }

    /**
     * @param token the token to be parsed
     * @return a map of the user id and phone number
     */
    @GetMapping("/verify")
    public Map<String, Object> verifyUserByToken(@RequestParam String token) {
        Map<String, Object> res;
        try {
            res = TokenUtils.verifyToken(token);
        } catch (TokenExpiredException e) {
            return null;
        }
        return res;
    }
}
