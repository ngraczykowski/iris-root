package com.silenteight.sep.usermanagement.keycloak.query;



import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;

import org.assertj.core.api.AbstractAssert;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class UserDtoAssert extends AbstractAssert<UserDtoAssert, UserDto> {

  protected UserDtoAssert(UserDto userDto) {
    super(userDto, UserDtoAssert.class);
  }

  static UserDtoAssert assertThatUserDto(UserDto userDto) {
    return new UserDtoAssert(userDto);
  }

  UserDtoAssert hasRoles(Map<String, List<String>> assertedRoles) {
    UserRoles userRoles = actual.getRoles();
    assertedRoles
        .entrySet()
        .forEach(
            entry -> assertThat(
                userRoles.getSortedRoles(entry.getKey())).isEqualTo(entry.getValue()));

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

  UserDtoAssert hasOrigin(String origin) {
    assertThat(actual).extracting(UserDto::getOrigin).isEqualTo(origin);

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
