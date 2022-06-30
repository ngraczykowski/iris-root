package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sep.usermanagement.api.role.RolesQuery;
import com.silenteight.sep.usermanagement.api.user.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
class UserRolesConfiguration {

  @Bean
  UserRolesRetriever userRolesRetriever(
      UserQuery userQuery, @Valid RolesProperties rolesProperties) {

    return new UserRolesRetriever(
        userQuery, rolesProperties.getRolesScope(), rolesProperties.getCountryGroupsScope());
  }

  @Bean
  ListRolesUseCase listRoleUseCase(RolesQuery rolesQuery, @Valid RolesProperties rolesProperties) {
    return new ListRolesUseCase(rolesQuery, rolesProperties.getRolesScope());
  }
}
