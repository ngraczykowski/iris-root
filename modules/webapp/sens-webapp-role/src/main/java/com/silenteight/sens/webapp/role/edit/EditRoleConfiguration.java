package com.silenteight.sens.webapp.role.edit;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EditRoleConfiguration {

  @Bean
  EditRoleUseCase editRoleUseCase(RoleService roleService, AuditingLogger auditingLogger) {
    return new EditRoleUseCase(roleService, auditingLogger);
  }
}
