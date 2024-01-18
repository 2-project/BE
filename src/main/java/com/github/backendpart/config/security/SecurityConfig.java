package com.github.backendpart.config.security;

import com.github.backendpart.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final String[] PERMIT_URL = {
            "/users/login",
            "/users/signup",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .csrf(AbstractHttpConfigurer::disable)  //csrf설정 끔
              .cors(AbstractHttpConfigurer::disable)  //cors설정 끔
              .sessionManagement((session) ->
                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )  //세션은 stateless방식
              .formLogin((auth) -> auth.disable())// formLogin 비활성화

              //예외처리
              .exceptionHandling((exception) ->
                  exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
              )
              .exceptionHandling((exception) -> exception.accessDeniedHandler(jwtAccessDeniedHandler))

               //인증 진행할 uri설정
              .authorizeHttpRequests((auth) ->
                  auth
                      .requestMatchers(PERMIT_URL).permitAll()
                      .requestMatchers("/api/**").authenticated()
                      .anyRequest()
                      .authenticated()
              )
              //jwt필터를 usernamepassword인증 전에 실행
              .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

      log.info("securityConfig");
      return http.build();
    }
}
