package com.silenteight.sens.webapp.user.roles.create;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateRoleConfiguration {

  @Bean
  CreateRoleUseCase createRoleUseCase() {
    return new CreateRoleUseCase();
  }
}
