package com.bouquet.api.config;


import com.bouquet.api.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Autowired
    private JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/oauth2/authorization/google", "/login/**" , "/logout");


    }

}