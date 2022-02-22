package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCase;
import com.silenteight.sep.usermanagement.api.role.RolesQuery;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRolesConfiguration {

  @Bean
  UserRolesRetriever userRolesRetriever(UserQuery userQuery, RolesProperties rolesProperties) {
    return new UserRolesRetriever(
        userQuery, rolesProperties.getRolesScope(), rolesProperties.getCountryGroupsScope());
  }

  @Bean
  ListRolesUseCase listRoleUseCase(RolesQuery rolesQuery, RolesProperties rolesProperties) {
    return new ListRolesUseCase(rolesQuery, rolesProperties.getRolesScope());
  }

  @Bean
  UserRolesValidator userRolesValidator(ListUsersWithRoleUseCase listUsersWithRoleUseCase) {
    return new UserRolesValidator(listUsersWithRoleUseCase);
  }
}
