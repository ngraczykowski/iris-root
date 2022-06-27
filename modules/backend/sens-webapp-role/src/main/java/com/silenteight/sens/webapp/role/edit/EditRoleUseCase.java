package com.silenteight.sens.webapp.role.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

@RequiredArgsConstructor
class EditRoleUseCase {

  @NonNull
  private final RoleService roleService;
  @NonNull
  private final AuditingLogger auditingLogger;

  void activate(@NonNull EditRoleRequest request) {
    request.preAudit(auditingLogger::log);
    roleService.update(request);
    request.postAudit(auditingLogger::log);
  }
}
