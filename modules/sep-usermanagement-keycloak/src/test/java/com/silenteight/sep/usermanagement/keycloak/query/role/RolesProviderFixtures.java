package com.silenteight.sep.usermanagement.keycloak.query.role;

import lombok.Value;
import lombok.experimental.UtilityClass;

import com.silenteight.sep.usermanagement.api.role.UserRoles;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Set.of;

@UtilityClass
public class RolesProviderFixtures {

  static final String SENS_USER_ROLE_SCOPE = "frontend";
  static final String AUDITOR = "Auditor";
  static final String ANALYST = "Analyst";
  static final String ROLE_NAME = "ROLE_NAME";
  static final String ROLE_SCOPE = "TEST_SCOPE";

  public static final Set<String> FRONTEND_USER_ROLES_1 = of(ANALYST, AUDITOR);
  public static final Set<String> FRONTEND_USER_ROLES_2 = emptySet();
  public static final Set<String> TEST_SCOPE_USER_ROLES = of(ROLE_NAME);

  static final RolesForUser USER_1_ROLES = new RolesForUser(
      "30da08c2-6fcc-4350-8ba1-a5ba7798b857", FRONTEND_USER_ROLES_1);

  static final RolesForUser USER_2_NO_ROLES = new RolesForUser(
      "f757bc03-ef62-4f69-a127-206ffc5b877c", FRONTEND_USER_ROLES_2);

  public static final UserRoles USER_ROLES_1 =
      new ExtractedUserRoles(Map.of(SENS_USER_ROLE_SCOPE, FRONTEND_USER_ROLES_1));

  public static final UserRoles USER_ROLES_2 =
      new ExtractedUserRoles(Map.of(SENS_USER_ROLE_SCOPE, FRONTEND_USER_ROLES_2));

  public static final UserRoles USER_ROLES_3 =
      new ExtractedUserRoles(Map.of(ROLE_SCOPE, TEST_SCOPE_USER_ROLES));

  @Value
  static class RolesForUser {

    String userId;
    Set<String> roles;
  }
}
