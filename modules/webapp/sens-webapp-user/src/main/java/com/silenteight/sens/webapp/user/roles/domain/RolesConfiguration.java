package com.silenteight.sens.webapp.user.roles.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RolesConfiguration {

  @Bean
  RolesQuery rolesQuery() {
    return new RolesQuery();
  }
}
