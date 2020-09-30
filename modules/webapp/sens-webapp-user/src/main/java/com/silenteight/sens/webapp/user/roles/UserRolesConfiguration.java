package com.silenteight.sens.webapp.user.roles;

import com.silenteight.sep.usermanagement.api.UserQuery;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserRolesConfiguration {

  @Bean
  UserRolesRetriever userRolesRetriever(UserQuery userQuery) {
    return new UserRolesRetriever(userQuery);
  }
}
