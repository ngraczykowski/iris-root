package com.silenteight.sens.webapp.role.remove;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RemoveRoleConfiguration {

  @Bean
  RemoveRoleUseCase removeRoleUseCase(
      RoleService roleService, AuditingLogger auditingLogger) {

    return new RemoveRoleUseCase(roleService, auditingLogger);
  }
}
