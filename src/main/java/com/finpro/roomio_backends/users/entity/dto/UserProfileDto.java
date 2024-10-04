package com.finpro.roomio_backends.users.entity.dto;

import com.finpro.roomio_backends.image.entity.dto.AvatarImageResponseDto;
import com.finpro.roomio_backends.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private boolean isTenant;
    private AvatarImageResponseDto avatar;
    private Date birthdate;
    private String phonenumber;
    private String method;

    public UserProfileDto(Users user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.isTenant = user.getIsTenant();
        this.avatar = user.getAvatar() == null ? null :new AvatarImageResponseDto(user.getAvatar());
        this.birthdate = user.getBirthdate();
        this.phonenumber = user.getPhonenumber();
        this.method = user.getMethod();

    }

    public UserProfileDto toDto(Users user) {
        return new UserProfileDto(user);
    }

}
