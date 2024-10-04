package com.finpro.roomio_backends.auth.service.impl;

import com.finpro.roomio_backends.auth.entity.UserAuth;
import com.finpro.roomio_backends.users.entity.Users;
import com.finpro.roomio_backends.users.repository.UsersRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UsersRepository usersRepository;

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    Optional<Users> userOpt = usersRepository.findByEmail(usernameOrEmail);
    if (userOpt.isEmpty()) {
      userOpt = usersRepository.findByEmail(usernameOrEmail);
    }
    Users user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

    return new UserAuth(user);
  }
}
