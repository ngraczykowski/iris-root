package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.usermanagement.keycloak.KeycloakUserAttributeNames;

import org.keycloak.representations.idm.UserRepresentation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static com.silenteight.sep.usermanagement.keycloak.origin.ExternalOrigin.EXTERNAL_ORIGIN;
import static java.time.OffsetDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

class KeycloakUserQueryTestFixtures {

  static final String SENS_USER_ROLE_SCOPE = "frontend";
  static final List<String> SENS_USER_ROLES = asList("ANALYST", "AUDITOR");

  static final KeycloakUser SENS_USER =
      new KeycloakUser("jdoe1", "John Doe", parse("2011-12-03T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"),
          Map.of(SENS_USER_ROLE_SCOPE, SENS_USER_ROLES), SENS_ORIGIN);
  static final KeycloakUser EXTERNAL_USER =
      new KeycloakUser("175698365", "8642367866", parse("2011-12-10T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"),
          Map.of(SENS_USER_ROLE_SCOPE, emptyList()), EXTERNAL_ORIGIN);

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
        Map<String, List<String>> roles,
        String origin) {

      this.userRepresentation = new UserRepresentation();
      this.userId = randomUUID().toString();

      userRepresentation.setId(userId);

      userRepresentation.setCreatedTimestamp(createdAt.toInstant().toEpochMilli());
      userRepresentation.setClientRoles(roles);
      userRepresentation.setFirstName(displayName);
      userRepresentation.setUsername(username);
      userRepresentation.singleAttribute(KeycloakUserAttributeNames.USER_ORIGIN, origin);
      this.lastLoginAtDate = lastLoginAt;
    }
  }
}
