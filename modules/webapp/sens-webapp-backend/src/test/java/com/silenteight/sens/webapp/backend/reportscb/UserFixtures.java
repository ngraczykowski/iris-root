package com.silenteight.sens.webapp.backend.reportscb;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.user.dto.UserDto;

import java.time.OffsetDateTime;
import java.util.Collection;

import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class UserFixtures {

  private static final OffsetDateTime DATE_TIME = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");

  static final Collection<UserDto> SINGLE_ROLE_USERS = asList(
      UserDto.builder()
          .userName("jannowak")
          .displayName("Jan Nowak")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(singletonList("admin"))
          .origin(SENS)
          .build(),
      UserDto.builder()
          .userName("adamkowalski")
          .displayName("Adam Kowalski")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(singletonList("analyst"))
          .origin(SENS)
          .build());

  static final Collection<UserDto> MULTIPLE_ROLES_USERS = asList(
      UserDto.builder()
          .userName("jannowak")
          .displayName("Jan Nowak")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(singletonList("admin"))
          .origin(SENS)
          .build(),
      UserDto.builder()
          .userName("adamkowalski")
          .displayName("Adam Kowalski")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .roles(asList("analyst", "auditor"))
          .origin(SENS)
          .build());
}
