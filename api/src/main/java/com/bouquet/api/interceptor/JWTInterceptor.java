//package com.bouquet.api.interceptor;
//
//
//import com.bouquet.api.util.JWTUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class JWTInterceptor implements HandlerInterceptor {
//    private static final String HEADER_AUTH = "access-token";
//
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
//        if (request.getMethod().equals("OPTIONS")) {
//            return true;
//        }
//
//        final String token = request.getHeader(HEADER_AUTH);
//        System.out.println("token : " + token);
//        if(token != null){
//            jwtUtil.valid(token);
//            return true;
//        }
//        throw new Exception("유효하지 않은 접근입니다.");
//    }
//
//}