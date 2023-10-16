package com.delicoffee.deli.util;


import com.delicoffee.deli.model.entity.DeliUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户拦截器，主要用于身份校验及身份信息的保存
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取session
        HttpSession session = request.getSession();
        // 2.获取session中的用户
        Object user = session.getAttribute("user");
        // 判断用户是否存在
        if (user == null){
            response.setStatus(401);
            return false;
        }
        // 存在，保存用户信息到ThreadLocal
        UserHolder.saveUser((DeliUser) user);
        return true;
    }

}
