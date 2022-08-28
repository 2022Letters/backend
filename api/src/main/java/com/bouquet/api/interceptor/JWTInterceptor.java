package com.bouquet.api.interceptor;


import com.bouquet.api.config.NoAuth;
import com.bouquet.api.user.exception.NotValidateAccessToken;
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

    // controller로 보내기 전에 처리하는 인터셉터
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // NoAuth 어노테이션이 있는 api 요청이면 통과
        boolean check=checkAnnotation(handler, NoAuth.class);
        if(check) return true;

        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        // header에 accessToken 이름으로 받은 token 값 확인
        final String token = request.getHeader(HEADER_AUTH);
        if(token != null){
            // 토큰 유효성 확인
            if(jwtUtil.validateTokenExpiration(token)){
                return true;
            }else{
                throw new NotValidateAccessToken();
            }
        }
        throw new Exception("유효하지 않은 접근입니다.");
    }

    // 어노테이션 체크하는 함수, 해당 어노테이션이 존재하는 api 요청 시 interceptor에 걸리지 않게 처리
    private boolean checkAnnotation(Object handler,Class cls){
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation(cls)!=null){ //해당 어노테이션이 존재하면 true.
            return true;
        }
        return false;
    }

}