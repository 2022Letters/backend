package com.bouquet.api.config;


import com.bouquet.api.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // allowCredentials를 true로 설정하여 allowedOrigins에 작성한 도메인과의 CORS 처리
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // JWTInterceptor 의존성 주입 후 사용
    @Autowired
    private JWTInterceptor jwtInterceptor;

    // interceptor 추가
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/oauth2/authorization/google", "/login/**" , "/logout", "/retoken");
    }

}