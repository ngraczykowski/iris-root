package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.sep.usermanagement.api.dto.UserDto;
import com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTestFixtures.KeycloakUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.LOCKED_AT;
import static com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTest.KeycloakUserQueryUserDtoAssert.assertThatUserDto;
import static java.lang.Integer.MAX_VALUE;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakUserQueryTest {

  public static final TimeConverter TIME_CONVERTER =
      new TimeConverter(MockTimeSource.ARBITRARY_INSTANCE);

  @Mock
  private UsersResource usersResource;

  private InMemoryTestLastLoginTimeProvider lastLoginTimeProvider =
      new InMemoryTestLastLoginTimeProvider();

  private InMemoryTestRoleProvider roleProvider = new InMemoryTestRoleProvider();

  private KeycloakUserQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQuery(
        usersResource, lastLoginTimeProvider, roleProvider, TIME_CONVERTER);
  }

  @Test
  void returnsAllUsers() {
    given(usersResource.list(0, MAX_VALUE))
        .willReturn(List.of(
            KeycloakUserQueryTestFixtures.SENS_USER.getUserRepresentation(),
            KeycloakUserQueryTestFixtures.EXTERNAL_USER.getUserRepresentation()));

    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getUserId(),
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getLastLoginAtDate());

    roleProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER_ROLES);

    Collection<UserDto> actual = underTest.listAll();

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.EXTERNAL_USER));
  }

  @Test
  void returnsLockedUser() {
    OffsetDateTime lockedAt = now();
    String userOrigin = "some_origin";
    String userName = "user1";
    String firstName = "John";
    OffsetDateTime createdAt = now().minusDays(1).withNano(0);

    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(userName);
    userRepresentation.setFirstName(firstName);
    userRepresentation.setCreatedTimestamp(createdAt.toInstant().toEpochMilli());
    userRepresentation.setAttributes(
        Map.of(
            USER_ORIGIN, List.of(userOrigin),
            LOCKED_AT, List.of(lockedAt.toString())));

    given(usersResource.list(0, MAX_VALUE)).willReturn(List.of(userRepresentation));

    List<UserDto> usersList = underTest.listAll();

    assertThat(usersList).hasSize(1);

    UserDto userDto = usersList.get(0);
    assertThat(userDto.getUserName()).isEqualTo(userName);
    assertThat(userDto.getDisplayName()).isEqualTo(firstName);
    assertThat(userDto.getCreatedAt()).isEqualTo(createdAt);
    assertThat(userDto.getOrigin()).isEqualTo(userOrigin);
    assertThat(userDto.getLockedAt()).isEqualTo(lockedAt);
  }

  static class KeycloakUserQueryUserDtoAssert extends UserDtoAssert {

    private KeycloakUserQueryUserDtoAssert(UserDto userDto) {
      super(userDto);
    }

    static KeycloakUserQueryUserDtoAssert assertThatUserDto(UserDto userDto) {
      return new KeycloakUserQueryUserDtoAssert(userDto);
    }

    private KeycloakUserQueryUserDtoAssert isEqualTo(KeycloakUser keycloakUser) {
      UserRepresentation userRepresentation = keycloakUser.getUserRepresentation();
      hasUsername(userRepresentation.getUsername());
      hasRoles(userRepresentation.getRealmRoles());
      hasDisplayName(userRepresentation.getFirstName());
      hasOrigin(getOrigin(userRepresentation));

      OffsetDateTime assertedCreationTime =
          TIME_CONVERTER.toOffsetFromMilli(userRepresentation.getCreatedTimestamp());
      hasCreatedAtTime(assertedCreationTime);

      hasLastLoginAtTime(keycloakUser.getLastLoginAtDate());

      return this;
    }

    private String getOrigin(UserRepresentation userRepresentation) {
      return userRepresentation.getAttributes().get(USER_ORIGIN).get(0);
    }
  }
}