package com.finpro.roomio_backends.users.entity.dto.changePassword;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {
  @NotNull
  @NotEmpty
  private String oldPassword;
  @NotNull
  @NotEmpty
  private String newPassword;
  @NotNull
  @NotEmpty
  private String confirmPassword;
}
