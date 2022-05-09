package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.api.role.UserRoles;
import com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames;

import org.keycloak.representations.idm.UserRepresentation;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.origin.ExternalOrigin.EXTERNAL_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_ROLES_1;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_ROLES_2;
import static com.silenteight.sep.usermanagement.keycloak.query.role.RolesProviderFixtures.USER_ROLES_3;
import static java.time.OffsetDateTime.parse;
import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class KeycloakUserQueryTestFixtures {

  static final String CLIENT_ID = "TEST_CLIENT";
  static final String ROLE_NAME = "ROLE_NAME";
  static final String ROLE_SCOPE = "TEST_SCOPE";

  static final KeycloakUser SENS_USER =
      new KeycloakUser(
          "jdoe1",
          "John Doe",
          parse("2011-12-03T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"),
          USER_ROLES_1,
          SENS_ORIGIN);
  static final KeycloakUser EXTERNAL_USER =
      new KeycloakUser(
          "175698365",
          "8642367866",
          parse("2011-12-10T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"),
          USER_ROLES_2,
          EXTERNAL_ORIGIN);
  static final KeycloakUser SENS_USER_2 =
      new KeycloakUser(
          "tom",
          "Tom Smith",
          parse("2011-12-03T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"),
          USER_ROLES_3,
          SENS_ORIGIN);

  @RequiredArgsConstructor
  @Getter
  static class KeycloakUser {

    private final UserRepresentation userRepresentation;
    @Nullable
    private final OffsetDateTime lastLoginAtDate;
    private final String userId;

    KeycloakUser(
        String username,
        String displayName,
        OffsetDateTime createdAt,
        @Nullable OffsetDateTime lastLoginAt,
        UserRoles roles,
        String origin) {

      this.userRepresentation = new UserRepresentation();
      this.userId = randomUUID().toString();

      userRepresentation.setId(userId);

      userRepresentation.setCreatedTimestamp(createdAt.toInstant().toEpochMilli());
      userRepresentation.setClientRoles(transformRoles(roles));
      userRepresentation.setFirstName(displayName);
      userRepresentation.setUsername(username);
      userRepresentation.singleAttribute(KeycloakUserAttributeNames.USER_ORIGIN, origin);
      this.lastLoginAtDate = lastLoginAt;
    }
  }

  private static Map<String, List<String>> transformRoles(UserRoles roles) {
    Map<String, List<String>> result = new HashMap<>();
    roles
        .getScopes()
        .forEach(clientId -> result.put(clientId, roles.getSortedRoles(clientId)));

    return result;
  }
}
