package com.silenteight.sens.webapp.backend.user.rest;

import lombok.experimental.UtilityClass;

import com.silenteight.sens.webapp.user.list.UserListDto;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sep.usermanagement.api.UserDomainError;
import com.silenteight.sep.usermanagement.api.UsernameUniquenessValidator.UsernameNotUniqueError;

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

  static final UserListDto ANALYST_USER = UserListDto.builder()
      .userName("analyst")
      .displayName("Analyst")
      .origin("SENS")
      .roles(singletonList("ANALYST"))
      .countryGroups(singletonList("SG"))
      .lastLoginAt(parse("2020-05-28T12:42:15+01:00"))
      .createdAt(parse("2020-05-20T10:15:30+01:00"))
      .build();

  static final UserListDto APPROVER_USER = UserListDto.builder()
      .userName("approver")
      .displayName("Approver")
      .origin("SENS")
      .roles(singletonList("APPROVER"))
      .countryGroups(singletonList("SG"))
      .lastLoginAt(parse("2020-06-12T11:32:10+01:00"))
      .createdAt(parse("2020-06-02T08:12:30+01:00"))
      .build();
}
