package com.silenteight.sens.webapp.role.create;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateRoleConfiguration {

  @Bean
  CreateRoleUseCase createRoleUseCase(
      RoleService roleService, AuditingLogger auditingLogger) {

    return new CreateRoleUseCase(roleService, auditingLogger);
  }
}
