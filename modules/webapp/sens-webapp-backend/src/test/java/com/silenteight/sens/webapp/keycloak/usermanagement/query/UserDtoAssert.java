package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.backend.user.dto.UserDto;

import org.assertj.core.api.AbstractAssert;

import java.time.OffsetDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.*;

class UserDtoAssert extends AbstractAssert<UserDtoAssert, UserDto> {

  protected UserDtoAssert(UserDto userDto) {
    super(userDto, UserDtoAssert.class);
  }

  static UserDtoAssert assertThatUserDto(UserDto userDto) {
    return new UserDtoAssert(userDto);
  }

  UserDtoAssert hasRoles(Collection<String> assertedRoles) {
    assertThat(actual.getRoles()).containsAll(assertedRoles);

    return this;
  }

  UserDtoAssert hasUsername(String username) {
    assertThat(actual).extracting(UserDto::getUserName).isEqualTo(username);

    return this;
  }

  UserDtoAssert hasDisplayName(String displayName) {
    assertThat(actual).extracting(UserDto::getDisplayName).isEqualTo(displayName);

    return this;
  }

  UserDtoAssert hasCreatedAtTime(OffsetDateTime assertedDate) {
    assertThat(actual).extracting(UserDto::getCreatedAt).isEqualTo(assertedDate);

    return this;
  }

  UserDtoAssert hasLastLoginAtTime(OffsetDateTime assertedDate) {
    assertThat(actual).extracting(UserDto::getLastLoginAt).isEqualTo(assertedDate);

    return this;
  }
}
