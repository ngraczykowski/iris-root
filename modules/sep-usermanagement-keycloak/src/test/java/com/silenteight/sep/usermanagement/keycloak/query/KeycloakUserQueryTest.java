package com.silenteight.sep.usermanagement.keycloak.query;

import com.silenteight.sep.base.common.time.TimeConverter;
import com.silenteight.sep.base.testing.time.MockTimeSource;
import com.silenteight.sep.usermanagement.api.user.dto.UserDto;
import com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTestFixtures.KeycloakUser;
import com.silenteight.sep.usermanagement.keycloak.query.client.ClientQuery;
import com.silenteight.sep.usermanagement.keycloak.query.role.InMemoryTestRoleProvider;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
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
import static com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTestFixtures.CLIENT_ID;
import static com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTestFixtures.ROLE_NAME;
import static com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTestFixtures.ROLE_SCOPE;
import static com.silenteight.sep.usermanagement.keycloak.query.KeycloakUserQueryTestFixtures.SENS_USER_2;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.FRONTEND_USER_ROLES_1;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_ROLES_1;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_ROLES_3;
import static java.lang.Integer.MAX_VALUE;
import static java.time.OffsetDateTime.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakUserQueryTest {

  private static final TimeConverter TIME_CONVERTER =
      new TimeConverter(MockTimeSource.ARBITRARY_INSTANCE);
  private static final String BLOKING_ROLE = "BLOKING_ROLE";
  private static final String OTHER_ROLE = "OTHER_ROLE";

  @Mock
  private UsersResource usersResource;
  @Mock
  private ClientsResource clientsResource;
  @Mock
  private ClientQuery clientQuery;
  @Mock
  private ClientResource clientResource;
  @Mock
  private ClientRepresentation clientRepresentation;
  @Mock
  private RolesResource rolesResource;
  @Mock
  private RoleResource roleResource;
  @Mock
  private UsersListFilter usersListFilter;

  private InMemoryTestLastLoginTimeProvider lastLoginTimeProvider =
      new InMemoryTestLastLoginTimeProvider();

  private InMemoryTestRoleProvider roleProvider = new InMemoryTestRoleProvider();

  private KeycloakUserQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new KeycloakUserQuery(
        usersResource,
        lastLoginTimeProvider,
        roleProvider,
        TIME_CONVERTER,
        clientsResource,
        clientQuery,
        usersListFilter);
    when(usersListFilter.isVisible(any())).thenReturn(true);
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

    roleProvider.add(KeycloakUserQueryTestFixtures.SENS_USER.getUserId(), USER_ROLES_1);

    Collection<UserDto> actual = underTest.listAll(FRONTEND_USER_ROLES_1);

    assertThat(actual)
        .hasSize(2)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.SENS_USER))
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(
            KeycloakUserQueryTestFixtures.EXTERNAL_USER));
  }

  @Test
  void returnUsersWithRoleName() {
    // given
    given(clientQuery.getByClientId(any())).willReturn(clientRepresentation);
    given(clientRepresentation.getId()).willReturn(CLIENT_ID);
    given(clientsResource.get(any())).willReturn(clientResource);
    given(clientResource.roles()).willReturn(rolesResource);
    given(rolesResource.get(any())).willReturn(roleResource);
    given(roleResource.getRoleUserMembers())
        .willReturn(of(SENS_USER_2.getUserRepresentation()));

    roleProvider.add(SENS_USER_2.getUserId(), USER_ROLES_3);
    lastLoginTimeProvider.add(SENS_USER_2.getUserId(), SENS_USER_2.getLastLoginAtDate());

    // when
    List<UserDto> actual = underTest.listAll(ROLE_NAME, ROLE_SCOPE);

    // then
    assertThat(actual)
        .hasSize(1)
        .anySatisfy(userDto -> assertThatUserDto(userDto).isEqualTo(SENS_USER_2));
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

    List<UserDto> usersList = underTest.listAll(FRONTEND_USER_ROLES_1);

    assertThat(usersList).hasSize(1);

    UserDto userDto = usersList.get(0);
    assertThat(userDto.getUserName()).isEqualTo(userName);
    assertThat(userDto.getDisplayName()).isEqualTo(firstName);
    assertThat(userDto.getCreatedAt()).isEqualTo(createdAt);
    assertThat(userDto.getOrigin()).isEqualTo(userOrigin);
    assertThat(userDto.getLockedAt()).isEqualTo(lockedAt);
  }

  @Test
  void filterUsersWithBlockingRole() {
    UserRepresentation userRepresentation1 = getUserRepresentation(
        "user1", singletonList(BLOKING_ROLE));
    UserRepresentation userRepresentation2 = getUserRepresentation(
        "user2", asList(BLOKING_ROLE, OTHER_ROLE));
    UserRepresentation userRepresentation3 = getUserRepresentation(
        "user3", singletonList(OTHER_ROLE));
    when(usersListFilter.isVisible(userRepresentation1)).thenReturn(false);
    when(usersListFilter.isVisible(userRepresentation2)).thenReturn(false);
    when(usersListFilter.isVisible(userRepresentation3)).thenReturn(true);

    given(usersResource.list(0, MAX_VALUE)).willReturn(
        List.of(userRepresentation1, userRepresentation2, userRepresentation3));

    List<UserDto> usersList = underTest.listAll(FRONTEND_USER_ROLES_1);

    assertThat(usersList).hasSize(1);

    UserDto userDto = usersList.get(0);
    assertThat(userDto.getUserName()).isEqualTo("user3");
  }

  @NotNull
  private UserRepresentation getUserRepresentation(String userName, List<String> realmRoles) {
    OffsetDateTime createdAt = now().minusDays(1).withNano(0);
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setId(userName);
    userRepresentation.setUsername(userName);
    userRepresentation.setRealmRoles(realmRoles);
    userRepresentation.setCreatedTimestamp(createdAt.toInstant().toEpochMilli());
    return userRepresentation;
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
      hasRoles(userRepresentation.getClientRoles());
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
