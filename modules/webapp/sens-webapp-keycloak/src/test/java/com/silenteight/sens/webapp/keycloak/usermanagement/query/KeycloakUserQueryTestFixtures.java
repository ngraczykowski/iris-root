package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.*;
import static com.silenteight.sens.webapp.user.domain.ExternalOrigin.EXTERNAL_ORIGIN;
import static com.silenteight.sens.webapp.user.domain.SensOrigin.SENS_ORIGIN;
import static java.time.OffsetDateTime.parse;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

class KeycloakUserQueryTestFixtures {

  static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);

  static final Pageable ONE_ELEMENT_PAGE_REQUEST = PageRequest.of(0, 1);

  static final List<String> SENS_USER_ROLES = asList("ANALYST", "AUDITOR");

  static final KeycloakUser SENS_USER =
      new KeycloakUser("jdoe1", "John Doe", parse("2011-12-03T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"), SENS_USER_ROLES, SENS_ORIGIN);
  static final KeycloakUser EXTERNAL_USER =
      new KeycloakUser("175698365", "8642367866", parse("2011-12-10T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"), emptyList(), EXTERNAL_ORIGIN);
  static final KeycloakUser DELETED_SENS_USER =
      new KeycloakUser("asmith", "Adam Smith", parse("2011-12-12T10:15:30+01:00"),
          parse("2011-12-12T15:15:30+01:00"), parse("2011-12-20T11:25:30+01:00"), emptyList(),
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
        List<String> roles,
        String origin) {

      this(username, displayName, createdAt, lastLoginAt, null, roles, origin);
    }

    KeycloakUser(
        String username,
        String displayName,
        OffsetDateTime createdAt,
        @Nullable OffsetDateTime lastLoginAt,
        @Nullable OffsetDateTime deletedAt,
        List<String> roles,
        String origin) {

      this.userRepresentation = new UserRepresentation();
      this.userId = randomUUID().toString();

      userRepresentation.setId(userId);

      userRepresentation.setCreatedTimestamp(createdAt.toInstant().toEpochMilli());
      userRepresentation.setRealmRoles(roles);
      userRepresentation.setFirstName(displayName);
      userRepresentation.setUsername(username);
      userRepresentation.singleAttribute(USER_ORIGIN, origin);

      if (deletedAt != null) {
        userRepresentation.setEnabled(false);
        userRepresentation.singleAttribute(DELETED_AT, deletedAt.toString());
      } else {
        userRepresentation.setEnabled(true);
      }

      this.lastLoginAtDate = lastLoginAt;
    }
  }
}
