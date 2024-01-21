package com.github.backendpart.web.entity.users;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CustomUserDetails implements UserDetails {

    private UserEntity user;

  public CustomUserDetails(UserEntity user) {
    this.user = user;
  }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      Collection<GrantedAuthority> collection = new ArrayList<>();
      collection.add(new SimpleGrantedAuthority(user.getRoles().toString()));
      return collection;
    }

    @Override
    public String getPassword() {
      return user.getUserId();
    }

    @Override
    public String getUsername() {
      return user.getUserPwd();
    }

    /* 계정 만료 여부
     * true :  만료 안됨
     * false : 만료
     */
    @Override
    public boolean isAccountNonExpired() {
      return false;
    }

    /* 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     */
    @Override
    public boolean isAccountNonLocked() {
      return false;
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
