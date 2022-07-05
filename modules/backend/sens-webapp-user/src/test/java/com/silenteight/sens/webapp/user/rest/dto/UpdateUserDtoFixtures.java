/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest.dto;

import lombok.experimental.UtilityClass;

import com.silenteight.sens.webapp.user.rest.dto.UpdateUserDto;

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
