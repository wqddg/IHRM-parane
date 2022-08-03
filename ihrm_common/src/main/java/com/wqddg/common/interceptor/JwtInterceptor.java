package com.wqddg.common.interceptor;

import com.wqddg.common.entity.ResultCode;
import com.wqddg.common.exception.CommonExcption;
import com.wqddg.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wqddg
 * @ClassName JwtHanler
 * @DateTime: 2022/1/6 22:49
 * @remarks : #自定义拦截器
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(authorization)){
            String token=authorization.replace("Bearer ","");
            Claims jwts = jwtUtils.getJwts(token);
            if (jwts!=null){
                request.setAttribute("user_claims",jwts);

                return true;
            }
        }
        throw new CommonExcption(ResultCode.UNAUTHENTICATED);
    }
}
