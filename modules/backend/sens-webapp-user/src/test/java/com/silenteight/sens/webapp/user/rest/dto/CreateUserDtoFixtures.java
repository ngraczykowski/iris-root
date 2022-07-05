/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.user.rest.dto;

import lombok.experimental.UtilityClass;

import com.silenteight.sens.webapp.user.rest.dto.CreateUserDto;

import static com.silenteight.sens.webapp.user.rest.DomainConstants.USER_FIELD_MAX_LENGTH;
import static com.silenteight.sens.webapp.user.rest.DomainConstants.USER_FIELD_MIN_LENGTH;


@UtilityClass
public class CreateUserDtoFixtures {

  public static final String USER_FIELD_TOO_LONG = "a".repeat(USER_FIELD_MAX_LENGTH + 1);
  public static final String USER_FIELD_TOO_SHORT = "a".repeat(USER_FIELD_MIN_LENGTH - 1);

  public static final CreateUserDto VALID_REQUEST =
      CreateUserDto.builder()
          .displayName("John Doe")
          .password("password")
          .userName("jdoe123")
          .build();

  public static final CreateUserDto INVALID_REQUEST_WITH_TOO_LONG_USER_NAME =
      CreateUserDto.builder()
          .displayName("John Doe")
          .password("password")
          .userName(USER_FIELD_TOO_LONG)
          .build();

  public static final CreateUserDto INVALID_REQUEST_WITH_TOO_SHORT_USER_NAME =
      CreateUserDto.builder()
          .displayName("John Doe")
          .password("password")
          .userName(USER_FIELD_TOO_SHORT)
          .build();

  public static final CreateUserDto INVALID_REQUEST_WITH_TOO_LONG_DISPLAY_NAME =
      CreateUserDto.builder()
          .displayName(USER_FIELD_TOO_LONG)
          .password("password")
          .userName("jdoe123")
          .build();

  public static final CreateUserDto INVALID_REQUEST_WITH_TOO_SHORT_DISPLAY_NAME =
      CreateUserDto.builder()
          .displayName(USER_FIELD_TOO_SHORT)
          .password("password")
          .userName("jdoe123")
          .build();

}
