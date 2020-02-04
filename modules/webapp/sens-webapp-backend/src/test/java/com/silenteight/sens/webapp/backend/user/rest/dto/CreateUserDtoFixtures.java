package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateUserDtoFixtures {

  public static final CreateUserDto VALID_REQUEST =
      CreateUserDto.builder()
          .displayName("John Doe")
          .password("password")
          .userName("jdoe123")
          .build();
}
