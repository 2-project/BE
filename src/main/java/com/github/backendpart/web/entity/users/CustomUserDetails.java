package com.github.backendpart.web.entity.users;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@AllArgsConstructor
@ToString
@Getter
public class CustomUserDetails implements UserDetails {

    private String userId;
    private String userPwd;
    private String userAddress;
    private String userPhone;
    private Collection<GrantedAuthority> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return role;
    }

    @Override
    public String getPassword() {
      return this.userPwd;
    }

    @Override
    public String getUsername() {
      return this.userId;
    }

    /* 계정 만료 여부
     * true :  만료 안됨
     * false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    /* 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    /* 비밀번호 만료 여부
     * true : 만료 안 됨
     * false : 만료
     */
    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    /* 사용자 활성화 여부
     * true : 활성화 됨
     * false : 활성화 안 됨
     */
    @Override
    public boolean isEnabled() {
      return true;
    }
}
