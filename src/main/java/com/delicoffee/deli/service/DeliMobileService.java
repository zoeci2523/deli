package com.delicoffee.deli.service;

/**
 * Service class for mobile
 */
public interface DeliMobileService {

    boolean checkMobileAndCode(String mobile, String verificationCode);

    boolean saveMobileToRedis(String mobile, String code);

    void sendCode(String mobile, String code);
}
