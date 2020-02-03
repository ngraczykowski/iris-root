package com.silenteight.sens.webapp.backend.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.user.dto.UserDto;

import java.time.OffsetDateTime;
import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsersFixtures {

  private static final OffsetDateTime DATE_TIME = OffsetDateTime.parse("2007-12-03T10:15:30+01:00");

  public static final Collection<UserDto> SINGLE_ROLE_USERS = asList(
      UserDto.builder()
          .userName("jannowak")
          .displayName("Jan Nowak")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .isActive(true)
          .roles(singletonList("admin"))
          .build(),
      UserDto.builder()
          .userName("adamkowalski")
          .displayName("Adam Kowalski")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .isActive(false)
          .roles(singletonList("analyst"))
          .build()
  );

  public static final Collection<UserDto> MULTIPLE_ROLES_USERS = asList(
      UserDto.builder()
          .userName("jannowak")
          .displayName("Jan Nowak")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .isActive(true)
          .roles(singletonList("admin"))
          .build(),
      UserDto.builder()
          .userName("adamkowalski")
          .displayName("Adam Kowalski")
          .createdAt(DATE_TIME)
          .lastLoginAt(DATE_TIME)
          .isActive(false)
          .roles(asList("analyst", "auditor"))
          .build()
  );
}
