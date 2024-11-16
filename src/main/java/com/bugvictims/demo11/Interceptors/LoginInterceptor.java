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
            if (claims != null) {
                int userId = (int) claims.get("userId"); // 把Token里的userID和用户名都放在setAttribute，方便Controller调用
                String userName = (String) claims.get("userName");
                request.setAttribute("userID", userId);
                request.setAttribute("userName", userName);
                return true;
            } else {
                response.setStatus(401);
                return false;
            }
        } catch (Exception e) {
            //设置http状态码401
            response.setStatus(401);
            return false;
        }

    }
}
