package com.silenteight.sens.webapp.keycloak.usermanagement.query.role;

import lombok.Value;
import lombok.experimental.UtilityClass;

import com.silenteight.commons.collections.MapBuilder;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@UtilityClass
class RolesProviderFixtures {

  static final String AUDITOR = "Auditor";
  static final String ANALYST = "Analyst";

  static final UserRoles USER_1_ROLES = new UserRoles(
      "30da08c2-6fcc-4350-8ba1-a5ba7798b857", asList(ANALYST, AUDITOR));

  static final UserRoles USER_2_NO_ROLES = new UserRoles(
      "f757bc03-ef62-4f69-a127-206ffc5b877c", emptyList());

  static final Map<String, List<String>> USERS = MapBuilder
      .from(USER_1_ROLES.userId, USER_1_ROLES.roles, USER_2_NO_ROLES.userId, USER_2_NO_ROLES.roles);

  @Value
  static class UserRoles {

    String userId;
    List<String> roles;
  }
}
