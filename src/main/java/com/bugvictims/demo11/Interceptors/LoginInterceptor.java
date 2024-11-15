package com.bugvictims.demo11.Interceptors;
import com.bugvictims.demo11.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        String token = request.getHeader("Authorization");
        //验证token
        try {
            Map<String, Object> claims = JWTUtils.validateToken(token);
            return true;
        } catch (Exception e) {
            //设置http状态码401
            response.setStatus(401);
            return false;
        }

    }
}
