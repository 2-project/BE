package com.github.backendpart.config.security.filter;

import com.github.backendpart.config.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String PREFIX = "Bearer";


    // SecurityContext에 Access Token으로부터 뽑아온 인증 정보를 저장.
    // SecurityContext는 어디서든 접근 가능한데, 정상적으로 Filter를 통과하여 Controller에 도착한다면, SecurityContext내부에 User의 username이 있다는 것이 보장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String jwt = resolveToken(request);

      if(StringUtils.hasText(jwt) && jwtTokenProvider.validationToken(jwt)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
    }

    //Header에서 Authorization부분을 추출할 때 Type이 Bearer인지 확인 후, 일치한다면 JWT부분만 추출하여 doFilter에 제공합니다.
    private String resolveToken(HttpServletRequest request) {
      String token = request.getHeader("Authorization");
      if(StringUtils.hasText(token) && token.startsWith(PREFIX)) {
        return token.substring(7);    // "Bearer "를 뺀 값, 즉 토큰 값
      }

      return null;
    }
}
