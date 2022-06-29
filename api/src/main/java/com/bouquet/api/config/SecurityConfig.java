package com.bouquet.api.config;


import com.bouquet.api.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;



@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .headers().frameOptions().disable() //

                .and()
                .authorizeRequests() //
                .antMatchers("/", "/css/**", "/images/**",
                        "/js/**", "/h2-console/**").permitAll()
                //.anyRequest().authenticated() //

                .and()
                .logout()
                .logoutSuccessUrl("/") //

                .and()
                .oauth2Login()
                .defaultSuccessUrl("/login/sucess")
                .userInfoEndpoint()
                .userService(customOAuth2UserService); //
    }

}
