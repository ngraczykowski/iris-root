package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import com.silenteight.sens.webapp.backend.user.dto.UserDto;
import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.common.time.TimeConverter;

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

import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTest.KeycloakUserQueryUserDtoAssert.assertThatUserDto;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTestFixtures.KeycloakUser;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTestFixtures.PAGE_REQUEST;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTestFixtures.USER_1;
import static com.silenteight.sens.webapp.keycloak.usermanagement.query.KeycloakUserQueryTestFixtures.USER_2;
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

  private KeycloakUserQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQuery(usersResource, lastLoginTimeProvider, TIME_CONVERTER);
  }

  @Test
  void returnsCorrectPage_givenTwoUsersInRepo() {
    given(usersResource.list((int) PAGE_REQUEST.getOffset(), PAGE_REQUEST.getPageSize()))
        .willReturn(List.of(USER_1.getUserRepresentation(), USER_2.getUserRepresentation()));

    lastLoginTimeProvider.add(USER_1.getUserId(), USER_1.getLastLoginAtDate());
    lastLoginTimeProvider.add(USER_2.getUserId(), USER_2.getLastLoginAtDate());

    Page<UserDto> actual = underTest.list(PAGE_REQUEST);

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(USER_1))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(USER_2));
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
      hasRoles(userRepresentation.getRealmRoles());
      hasDisplayName(userRepresentation.getFirstName());
      hasUsername(userRepresentation.getUsername());

      OffsetDateTime assertedCreationTime =
          TIME_CONVERTER.toOffsetFromSeconds(userRepresentation.getCreatedTimestamp());
      hasCreatedAtTime(assertedCreationTime);

      hasLastLoginAtTime(keycloakUser.getLastLoginAtDate());

      return this;
    }
  }
}
