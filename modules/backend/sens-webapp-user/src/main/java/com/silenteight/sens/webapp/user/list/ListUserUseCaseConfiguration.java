package com.silenteight.sens.webapp.user.list;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListUserUseCaseConfiguration {

  @Bean
  ListUsersUseCase listUserUseCase(UserQuery userQuery, RolesProperties rolesProperties) {
    return new ListUsersUseCase(
        userQuery, rolesProperties.getRolesScope(), rolesProperties.getCountryGroupsScope());
  }

  @Bean
  ListUsersWithRoleUseCase listUsersInRoleUseCase(
      UserQuery userQuery, RolesProperties rolesProperties) {

    return new ListUsersWithRoleUseCase(userQuery, rolesProperties.getRolesScope());
  }
}
