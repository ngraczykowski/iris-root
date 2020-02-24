package com.silenteight.sens.webapp.keycloak.usermanagement.query;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.user.domain.UserOrigin;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.keycloak.usermanagement.KeycloakUserAttributeNames.ORIGIN;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.GNS;
import static com.silenteight.sens.webapp.user.domain.UserOrigin.SENS;
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
          parse("2011-12-10T15:15:30+01:00"), SENS_USER_ROLES, SENS);
  static final KeycloakUser GNS_USER =
      new KeycloakUser("jdoe1", "John Doe", parse("2011-12-10T10:15:30+01:00"),
          parse("2011-12-10T15:15:30+01:00"), emptyList(), GNS);

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
        UserOrigin origin) {

      this.userRepresentation = new UserRepresentation();
      this.userId = randomUUID().toString();

      userRepresentation.setId(userId);
      userRepresentation.setEnabled(true);
      userRepresentation.setCreatedTimestamp(createdAt.toInstant().toEpochMilli());
      userRepresentation.setRealmRoles(roles);
      userRepresentation.setFirstName(displayName);
      userRepresentation.setUsername(username);
      userRepresentation.singleAttribute(ORIGIN, origin.toString());

      this.lastLoginAtDate = lastLoginAt;
    }
  }
}
