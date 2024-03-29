package com.github.backendpart.config.security;

import org.springframework.http.HttpMethod;
import com.github.backendpart.config.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 인증은 필요없는 api
    private final String[] PERMIT_URL = {
            "/auth/login",
            "/auth/signup",
            "/auth/signup/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs/**",
            "/api/product/**",
            "/v3/**"
    };

    // 관리자 권한이 필요한 api
    private final String[] ADMIN_URL = {
            "api/admin/**"
    };

    // 인증이 필요한 api
    private final String[] AUTHENTICATION_URL = {
            "/api/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
    }

    // cors 세팅
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","PUT","POST","PATCH","DELETE","OPTIONS"));
        corsConfiguration.setMaxAge(1000L*60*60);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
              .csrf((csrf) -> csrf.disable())  //csrf설정 끔
              .cors((cors) -> cors.disable())  //cors설정 끔
              .sessionManagement((session) ->
                      session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              )  //세션은 stateless방식
              .formLogin((auth) -> auth.disable())// formLogin 비활성화
              .rememberMe((remember) -> remember.disable())
              //cors 재설정
              .cors((cors) -> {
                  cors.configurationSource(corsConfigurationSource());
              })
              //예외처리
              .exceptionHandling((exception) ->
                  exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
              )
              .exceptionHandling((exception) -> exception.accessDeniedHandler(jwtAccessDeniedHandler))

               //인증 진행할 uri설정
              .authorizeHttpRequests((auth) ->
                  auth
                          .requestMatchers(PERMIT_URL).permitAll()
                          .requestMatchers(ADMIN_URL).hasRole("ADMIN")
                          .requestMatchers(AUTHENTICATION_URL).authenticated()
                          .anyRequest().hasRole("ADMIN")
              )
              .logout((logout) -> {
                  logout.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"));
                  logout.logoutSuccessUrl("/auth/login");
                  logout.invalidateHttpSession(true);
              })
              //jwt필터를 usernamepassword인증 전에 실행
              .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

      log.info("securityConfig");
      return http.build();
    }

}
