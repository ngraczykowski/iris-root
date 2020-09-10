package com.silenteight.sep.keycloak.query;

import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.sep.keycloak.KeycloakUserAttributeNames;
import com.silenteight.sep.keycloak.query.KeycloakUserQueryTestFixtures.KeycloakUser;

import com.sillenteight.sep.usermanagement.api.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import static com.silenteight.sep.keycloak.query.KeycloakUserQueryTest.KeycloakUserQueryUserDtoAssert.assertThatUserDto;
import static java.lang.Integer.MAX_VALUE;
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

    given(usersResource.list(0, MAX_VALUE))
        .willReturn(List.of(
            KeycloakUserQueryTestFixtures.SENS_USER.getUserRepresentation(),
            KeycloakUserQueryTestFixtures.EXTERNAL_USER.getUserRepresentation(),
            KeycloakUserQueryTestFixtures.DELETED_SENS_USER.getUserRepresentation()));
  }

  @Test
  void returnsCorrectPage_givenThreeUsersInRepoAndPageRequest() {
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getUserId(),
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getLastLoginAtDate());

    roleProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER_ROLES);

    Page<UserDto> actual = underTest.listEnabled(KeycloakUserQueryTestFixtures.PAGE_REQUEST);

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.EXTERNAL_USER));
  }

  @Test
  void returnsCorrectPage_givenThreeUsersInRepoAndOneElementPageRequest() {
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER.getLastLoginAtDate());

    roleProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER_ROLES);

    Page<UserDto> actual = underTest.listEnabled(
        KeycloakUserQueryTestFixtures.ONE_ELEMENT_PAGE_REQUEST);

    assertThat(actual)
        .hasSize(1)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.SENS_USER));
  }

  @Test
  void returnsEnabledUsers_givenThreeUsersInRepo() {
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getUserId(),
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getLastLoginAtDate());

    roleProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER_ROLES);

    Collection<UserDto> actual = underTest.listEnabled();

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.EXTERNAL_USER));
  }

  @Test
  void returnsAllUsers_givenThreeUsersInRepo() {
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getUserId(),
        KeycloakUserQueryTestFixtures.EXTERNAL_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(
        KeycloakUserQueryTestFixtures.DELETED_SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.DELETED_SENS_USER.getLastLoginAtDate());

    roleProvider.add(
        KeycloakUserQueryTestFixtures.SENS_USER.getUserId(),
        KeycloakUserQueryTestFixtures.SENS_USER_ROLES);

    Collection<UserDto> actual = underTest.listAll();

    assertThat(actual)
        .hasSize(3)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.EXTERNAL_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.DELETED_SENS_USER));
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
      return userRepresentation.getAttributes().get(KeycloakUserAttributeNames.USER_ORIGIN).get(0);
    }
  }
}
