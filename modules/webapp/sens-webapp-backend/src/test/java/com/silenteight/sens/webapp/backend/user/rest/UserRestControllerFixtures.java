package com.silenteight.sens.webapp.backend.user.rest;

import lombok.experimental.UtilityClass;

import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.domain.validator.UsernameUniquenessValidator.UsernameNotUniqueError;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;

import static java.time.OffsetDateTime.parse;
import static java.util.Collections.singletonList;

@UtilityClass
class UserRestControllerFixtures {

  static final String USERNAME = "jdoe123";
  static final RegisterInternalUserUseCase.Success USER_REGISTRATION_SUCCESS =
      () -> USERNAME;

  static final String USER_REGISTRATION_ERROR_REASON = "some reason";

  static final UserDomainError USER_REGISTRATION_DOMAIN_ERROR =
      () -> USER_REGISTRATION_ERROR_REASON;

  static final UsernameNotUniqueError USERNAME_NOT_UNIQUE = new UsernameNotUniqueError(USERNAME);

  static final UserDto ANALYST_USER = UserDto.builder()
      .userName("analyst")
      .displayName("Analyst")
      .roles(singletonList("ANALYST"))
      .lastLoginAt(parse("2020-05-28T12:42:15+01:00"))
      .createdAt(parse("2020-05-20T10:15:30+01:00"))
      .origin("SENS")
      .build();

  static final UserDto APPROVER_USER = UserDto.builder()
      .userName("approver")
      .displayName("Approver")
      .roles(singletonList("APPROVER"))
      .lastLoginAt(parse("2020-06-12T11:32:10+01:00"))
      .createdAt(parse("2020-06-02T08:12:30+01:00"))
      .origin("SENS")
      .build();
}
