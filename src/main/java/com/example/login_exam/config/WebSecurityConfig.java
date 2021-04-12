package com.example.login_exam.config;

import com.example.login_exam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/signup", "/user").permitAll()    // 누구나 접근 허용
                .antMatchers("/").hasRole("USER")                       // USER, ADMIN 만 접근가능
                .antMatchers("/admin").hasRole("ADMIN")                 // ADMIN 만 접근 가능
                .anyRequest().authenticated()       // 나머지 요청들은 권한의 종류에 상관없이 권한이 있어야 접근가능
                .and()
                .formLogin()
                .loginPage("/login")        // 로그인 페이지 링크
                .defaultSuccessUrl("/")     // 로그인 성공 후 리다이렉트 주소
                .and()
                .logout()
                .logoutSuccessUrl("/login")     // 로그아웃 성공 시 리다이렉트 주소
                .invalidateHttpSession(true);   // 세션 날리기
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                // 해당 서비스(userService) 에서는 UserDetailService 를 implements 해서
                // loadUserByUsername() 구현해야함. (서비스 참고)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
