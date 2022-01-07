package com.silenteight.sens.webapp.user.roles.delete;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteRoleConfiguration {

  @Bean
  DeleteRoleUseCase deleteRoleUseCase() {
    return new DeleteRoleUseCase();
  }
}
