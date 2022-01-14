package com.silenteight.sens.webapp.user.roles.edit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EditRoleConfiguration {

  @Bean
  EditRoleUseCase editRoleUseCase() {
    return new EditRoleUseCase();
  }
}
