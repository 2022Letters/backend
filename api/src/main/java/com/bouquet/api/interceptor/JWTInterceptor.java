package com.bouquet.api.interceptor;


import com.bouquet.api.config.NoAuth;
import com.bouquet.api.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    private static final String HEADER_AUTH = "accessToken";


    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println(request.getRequestURL()+"url확인용");
        System.out.println(request.getRequestURI()+"uri확인용");

        boolean check=checkAnnotation(handler, NoAuth.class);
        if(check) return true;

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        final String token = request.getHeader(HEADER_AUTH);
        System.out.println("token : " + token);
        if(token != null){
            jwtUtil.valid(token);
            return true;
        }
        throw new Exception("유효하지 않은 접근입니다.");
    }

    private boolean checkAnnotation(Object handler,Class cls){
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation(cls)!=null){ //해당 어노테이션이 존재하면 true.
            return true;
        }
        return false;
    }

}