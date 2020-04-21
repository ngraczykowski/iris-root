package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.common.time.TimeConverter;
import com.silenteight.sens.webapp.user.domain.UserOrigin;
import com.silenteight.sens.webapp.user.dto.UserDto;

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

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.USER_ORIGIN;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTest.KeycloakUserQueryUserDtoAssert.assertThatUserDto;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTestFixtures.*;
import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class KeycloakUserQueryTest {

  public static final TimeConverter TIME_CONVERTER =
      new TimeConverter(MockTimeSource.ARBITRARY_INSTANCE);

  @Mock
  private UsersResource usersResource;

  @Mock
  private AuditLog auditLog;

  private InMemoryTestLastLoginTimeProvider lastLoginTimeProvider =
      new InMemoryTestLastLoginTimeProvider();

  private InMemoryTestRoleProvider roleProvider = new InMemoryTestRoleProvider();

  private KeycloakUserQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQuery(
        usersResource, lastLoginTimeProvider, roleProvider, TIME_CONVERTER, auditLog);

    given(usersResource.list(0, MAX_VALUE))
        .willReturn(List.of(
            SENS_USER.getUserRepresentation(),
            GNS_USER.getUserRepresentation(),
            DELETED_SENS_USER.getUserRepresentation()));
  }

  @Test
  void returnsCorrectPage_givenThreeUsersInRepoAndPageRequest() {
    lastLoginTimeProvider.add(SENS_USER.getUserId(), SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(GNS_USER.getUserId(), GNS_USER.getLastLoginAtDate());

    roleProvider.add(SENS_USER.getUserId(), SENS_USER_ROLES);

    Page<UserDto> actual = underTest.listEnabled(PAGE_REQUEST);

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(GNS_USER));
  }

  @Test
  void returnsCorrectPage_givenThreeUsersInRepoAndOneElementPageRequest() {
    lastLoginTimeProvider.add(SENS_USER.getUserId(), SENS_USER.getLastLoginAtDate());

    roleProvider.add(SENS_USER.getUserId(), SENS_USER_ROLES);

    Page<UserDto> actual = underTest.listEnabled(ONE_ELEMENT_PAGE_REQUEST);

    assertThat(actual)
        .hasSize(1)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(SENS_USER));
  }

  @Test
  void returnsEnabledUsers_givenThreeUsersInRepo() {
    lastLoginTimeProvider.add(SENS_USER.getUserId(), SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(GNS_USER.getUserId(), GNS_USER.getLastLoginAtDate());

    roleProvider.add(SENS_USER.getUserId(), SENS_USER_ROLES);

    Collection<UserDto> actual = underTest.listEnabled();

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(GNS_USER));
  }

  @Test
  void returnsAllUsers_givenThreeUsersInRepo() {
    lastLoginTimeProvider.add(SENS_USER.getUserId(), SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(GNS_USER.getUserId(), GNS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(
        DELETED_SENS_USER.getUserId(), DELETED_SENS_USER.getLastLoginAtDate());

    roleProvider.add(SENS_USER.getUserId(), SENS_USER_ROLES);

    Collection<UserDto> actual = underTest.listAll();

    assertThat(actual)
        .hasSize(3)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(GNS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(DELETED_SENS_USER));
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

    private UserOrigin getOrigin(UserRepresentation userRepresentation) {
      return UserOrigin.valueOf(userRepresentation.getAttributes().get(USER_ORIGIN).get(0));
    }
  }
}
