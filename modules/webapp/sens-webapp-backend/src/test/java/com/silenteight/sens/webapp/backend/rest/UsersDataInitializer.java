package com.silenteight.sens.webapp.backend.rest;

import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.user.UserService;
import com.silenteight.sens.webapp.users.user.dto.CreateUserRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_ANALYST;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_APPROVER;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_MAKER;
import static com.silenteight.sens.webapp.kernel.security.authority.Role.ROLE_USER_MANAGER;
import static java.util.Arrays.asList;

@Component
class UsersDataInitializer {

  static final String SUPERUSER = "superuser";
  static final String MAKER_2 = "maker_2";
  static final String MAKER_1 = "maker_1";
  static final String APPROVER_2 = "approver_2";
  static final String APPROVER_1 = "approver_1";
  static final String ANALYST = "analyst";
  static final String USER_MANAGER = "user_manager";

  @Autowired
  private UserService userService;

  private Map<String, Long> users = new ConcurrentHashMap<>();

  void initSensUsers() {
    createUser(APPROVER_1, false, ROLE_APPROVER);
    createUser(APPROVER_2, false, ROLE_APPROVER);
    createUser(MAKER_1, false, ROLE_MAKER);
    createUser(MAKER_2, false, ROLE_MAKER);
    createUser(SUPERUSER, true);
    createUser(ANALYST, false, ROLE_ANALYST);
    createUser(USER_MANAGER, false, ROLE_USER_MANAGER);
  }

  void cleanUpSensUsers() {
    userService.delete(APPROVER_1);
    userService.delete(APPROVER_2);
    userService.delete(MAKER_1);
    userService.delete(MAKER_2);
    userService.delete(SUPERUSER);
    userService.delete(ANALYST);
    userService.delete(USER_MANAGER);
  }

  private void createUser(String name, boolean isSuperUser, Role... roles) {
    CreateUserRequest userRequest = CreateUserRequest
        .builder()
        .name(name)
        .password("pass")
        .displayName("displayName")
        .superUser(isSuperUser)
        .roles(asList(roles))
        .build();
    Long id = userService.create(userRequest);
    users.put(name, id);
  }
}
