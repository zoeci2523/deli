package com.delicoffee.deli.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.delicoffee.deli.common.Constant;
import com.delicoffee.deli.exception.DeliCoffeeException;
import com.delicoffee.deli.exception.DeliCoffeeExceptionEnum;
import com.delicoffee.deli.model.entity.DeliUser;
import com.delicoffee.deli.util.UserHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

public class UserFilter implements Filter {

    //public static DeliUser currentUser = new User();
    // 使用ThreadLocal获得user
    public static DeliUser currentUser = new DeliUser();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 获取客户端携带的 token
        String token = request.getHeader(Constant.JWT_TOKEN);
        if (token == null){
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n+" +
                    " \"status\": 10007,\n" +
                    " \"msg\": NEED_JWT_TOKEN,\n" +
                    " \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        // 校验token的真实性
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try{
            // 解密JWT，获取里面的用户信息
            DecodedJWT jwt = verifier.verify(token);
            if(jwt.getClaim(Constant.USER_ID).asInt().equals("")){
                PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
                out.write("{\n+" +
                        " \"status\": 10007,\n" +
                        " \"msg\": NEED_LOGIN,\n" +
                        " \"data\": null\n" +
                        "}");
                out.flush();
                out.close();
                return;
            }
            currentUser.setId(jwt.getClaim(Constant.USER_ID).asInt());
            currentUser.setUsername(jwt.getClaim(Constant.USER_NAME).asString());
            currentUser.setRole(jwt.getClaim(Constant.USER_ROLE).asInt());
            UserHolder.saveUser(currentUser);
        }catch (TokenExpiredException e){
            // token 过期
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.TOKEN_EXPIRED);
        }catch (JWTDecodeException e){
            // 解码失败
            throw new DeliCoffeeException(DeliCoffeeExceptionEnum.TOKEN_WRONG);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
