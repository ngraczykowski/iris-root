package com.silenteight.sens.webapp.backend.user.rest.dto;

import lombok.experimental.UtilityClass;

import static java.util.Collections.singleton;

@UtilityClass
public class UpdateUserDtoFixtures {

  public static final UpdateUserDto VALID_UPDATE_REQUEST = UpdateUserDto
      .builder()
      .displayName("John Doe")
      .roles(singleton("User Administrator"))
      .build();

  public static final UpdateUserDto VALID_UPDATE_DISPLAYNAME_REQUEST = UpdateUserDto
      .builder()
      .displayName("John Doe")
      .build();

  public static final UpdateUserDto VALID_UPDATE_ROLES_REQUEST = UpdateUserDto
      .builder()
      .roles(singleton("User Administrator"))
      .build();
}
