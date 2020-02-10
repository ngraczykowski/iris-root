package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import javax.annotation.Nullable;

import static java.time.OffsetDateTime.parse;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

class KeycloakUserQueryTestFixtures {

  static final Pageable PAGE_REQUEST = PageRequest.of(0, 5);

  static final KeycloakUser USER_1 =
      new KeycloakUser("jdoe1", "John Doe", parse("2011-12-03T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"));
  static final KeycloakUser USER_2 =
      new KeycloakUser("jdoe1", "John Doe", parse("2011-12-10T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"));

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
        @Nullable OffsetDateTime lastLoginAt) {
      this.userRepresentation = new UserRepresentation();
      this.userId = randomUUID().toString();

      userRepresentation.setId(userId);
      userRepresentation.setCreatedTimestamp(createdAt.toEpochSecond());
      userRepresentation.setRealmRoles(emptyList());
      userRepresentation.setFirstName(displayName);
      userRepresentation.setUsername(username);

      this.lastLoginAtDate = lastLoginAt;
    }
  }
}
