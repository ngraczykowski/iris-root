package com.silenteight.sens.webapp.role.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

@RequiredArgsConstructor
class CreateRoleUseCase {

  @NonNull
  private final RoleService roleService;
  @NonNull
  private final AuditingLogger auditingLogger;

  void activate(@NonNull CreateRoleRequest request) {
    request.preAudit(auditingLogger::log);
    roleService.create(request);
    request.postAudit(auditingLogger::log);
  }
}
