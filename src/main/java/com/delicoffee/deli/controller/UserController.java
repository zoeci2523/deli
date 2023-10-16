package com.delicoffee.deli.controller;

import cn.hutool.core.util.RandomUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.delicoffee.deli.common.ApiRestResponse;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.model.entity.DeliUser;
import com.delicoffee.deli.service.DeliMobileService;
import com.delicoffee.deli.service.DeliUserService;
import com.delicoffee.deli.util.RegexUtils;
import com.delicoffee.deli.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller
public class UserController {

    @Autowired
    DeliUserService userService;

    @Autowired
    DeliMobileService mobileService;

    @PostMapping("/register")
    @ResponseBody
    public ApiRestResponse register(@RequestParam("username") String username,
                                    @RequestParam("password")String password,
                                    @RequestParam("mobile")String mobile,
                                    @RequestParam("verificationCode")String code) throws NoSuchAlgorithmException {
        // 传入字符串为空或空格
        if (StringUtils.isEmpty(username)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_PASSWORD);
        }
        if (StringUtils.isEmpty(mobile)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_MOBILE);
        }
        if (StringUtils.isEmpty(code)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_VERIFICATION_CODE);
        }

        // 密码长度校验
        if (password.length() < 8){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.PASSWORD_TOO_SHORT);
        }
        // 不允许手机号重复注册
        boolean mobilePassed = userService.checkMobileRegistered(mobile);
        if (!mobilePassed){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.MOBILE_ALREADY_BEEN_REGISTERED);
        }
        // 校验手机和验证码是否匹配
        boolean mobileAndCodePassed = mobileService.checkMobileAndCode(mobile, code);
        if (!mobileAndCodePassed){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.WRONG_VERIFICATION_CODE);
        }
        userService.register(username, password, mobile);
        return ApiRestResponse.success();
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("username") String username,
                                    @RequestParam("password")String password, HttpSession session){
        if (StringUtils.isEmpty(username)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_PASSWORD);
        }
        DeliUser user = userService.login(username, password);
        user.setPassword(null);
        session.setAttribute(Constant.DELI_USER, user);
        return ApiRestResponse.success(user);
    }
    /**
     * 登出，清除session
     * @param session
     * @return
     */
    @PostMapping("/user/logout")
    @ResponseBody
    public ApiRestResponse logout(HttpSession session){
        session.removeAttribute(Constant.DELI_USER);
        UserHolder.removeUser();
        return ApiRestResponse.success();
    }

    /**
     * 发送邮件
     */
    @PostMapping("/user/sendCode")
    @ResponseBody
    public ApiRestResponse sendCode(@RequestParam("mobile") String mobile){
        // 1.校验手机号是否有效，且已注册，如果不符合返回错误信息
        boolean validMobile = RegexUtils.isPhoneInvalid(mobile);
        if (validMobile){
            // 2.生成校验码
            // 3.保存验证码到session
            // 4.发送验证码
            boolean mobilePassed = userService.checkMobileRegistered(mobile);
            if (!mobilePassed){
                return ApiRestResponse.error(DeliCoffeeExceptionEnum.MOBILE_ALREADY_BEEN_REGISTERED);
            }else {
                String code = RandomUtil.randomNumbers(6);
                Boolean saveToRedis = mobileService.saveMobileToRedis(mobile, code);
                if (saveToRedis){
                    mobileService.sendCode(mobile, code);
                    return ApiRestResponse.success();
                }else {
                    return ApiRestResponse.error(DeliCoffeeExceptionEnum.MOBILE_ALREADY_BEEN_SEND);
                }
            }
        }else {
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.WRONG_MOBILE);
        }
    }

    @PostMapping("/loginWithJwt")
    @ResponseBody
    public ApiRestResponse loginWithJwt(@RequestParam("username") String username,
                                        @RequestParam("password")String password){
        if (StringUtils.isEmpty(username)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isEmpty(password)){
            return ApiRestResponse.error(DeliCoffeeExceptionEnum.NEED_PASSWORD);
        }
        DeliUser user = userService.login(username, password);
        user.setPassword(null);
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        // 生成JWT
        String token = JWT.create().withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                // 过期时间
                .withExpiresAt(new Date(System.currentTimeMillis()+Constant.EXPIRE_TIME))
                .sign(algorithm);
        UserHolder.saveUser(user);
        return ApiRestResponse.success(token);
    }

    /**
     * 登出，将jwt token设为空
     */
    @PostMapping("/user/logoutWithJwt")
    @ResponseBody
    public ApiRestResponse logoutWithJwt(){
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_ID, "")
                .withClaim(Constant.USER_NAME, "")
                .withClaim(Constant.USER_ROLE, "")
                // 指定过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        UserHolder.removeUser();
        return ApiRestResponse.success(token);

    }
}
