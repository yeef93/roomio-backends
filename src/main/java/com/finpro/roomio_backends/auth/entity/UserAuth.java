package com.finpro.roomio_backends.auth.entity;


import com.finpro.roomio_backends.users.entity.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class UserAuth extends Users implements UserDetails {

  private final Users user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    if (user.getEmail().equals("yunindafaranika@gmail.comx")) {
      authorities.add(() -> "ROLE_SUPERMAN");
    } else if (user.getIsTenant()) {
      authorities.add(() -> "ROLE_TENANT");
    } else {
      authorities.add(() -> "ROLE_USER");
    }
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public Boolean getIsTenant() {
    return user.getIsTenant();
  }
}
