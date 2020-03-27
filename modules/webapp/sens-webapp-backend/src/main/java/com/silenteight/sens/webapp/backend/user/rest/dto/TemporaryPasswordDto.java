package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.sens.webapp.user.password.reset.TemporaryPassword;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporaryPasswordDto {

  String temporaryPassword;

  public static TemporaryPasswordDto from(TemporaryPassword tempPassword) {
    return new TemporaryPasswordDto(tempPassword.getPassword());
  }
}
