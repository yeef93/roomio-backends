package com.finpro.roomio_backends.users.entity.dto.userManagement;

import com.finpro.roomio_backends.users.entity.Users;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;

@Data
@RequiredArgsConstructor
public class ProfileUpdateRequestDTO {

  private String firstname;
  private String lastname;
  private Long avatarId;
  private String phonenumber;
  private Date birthdate;

  public Users dtoToEntity(Users user, ProfileUpdateRequestDTO requestDto) {
    Optional.ofNullable(requestDto.getFirstname()).ifPresent(user::setFirstname);
    Optional.ofNullable(requestDto.getLastname()).ifPresent(user::setLastname);
    Optional.ofNullable(requestDto.getPhonenumber()).ifPresent(user::setPhonenumber);
    Optional.ofNullable(requestDto.getBirthdate()).ifPresent(user::setBirthdate);
    return user;
  }

}
