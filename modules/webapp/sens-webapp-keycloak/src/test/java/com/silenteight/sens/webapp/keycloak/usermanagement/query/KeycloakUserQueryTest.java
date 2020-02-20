package com.silenteight.sens.webapp.keycloak.usermanagement.query;

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
import java.util.List;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.ORIGIN;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTest.KeycloakUserQueryUserDtoAssert.assertThatUserDto;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTestFixtures.*;
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
  void returnsCorrectPage_givenTwoUsersInRepo() {
    given(usersResource.list((int) PAGE_REQUEST.getOffset(), PAGE_REQUEST.getPageSize()))
        .willReturn(List.of(SENS_USER.getUserRepresentation(), GNS_USER.getUserRepresentation()));

    lastLoginTimeProvider.add(SENS_USER.getUserId(), SENS_USER.getLastLoginAtDate());
    lastLoginTimeProvider.add(GNS_USER.getUserId(), GNS_USER.getLastLoginAtDate());

    roleProvider.add(SENS_USER.getUserId(), SENS_USER_ROLES);

    Page<UserDto> actual = underTest.list(PAGE_REQUEST);

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(GNS_USER));
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
      return UserOrigin.valueOf(userRepresentation.getAttributes().get(ORIGIN).get(0));
    }
  }
}
