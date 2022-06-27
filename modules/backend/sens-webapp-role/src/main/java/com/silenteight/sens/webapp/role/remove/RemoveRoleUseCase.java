package com.silenteight.sens.webapp.role.remove;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sens.webapp.role.domain.RoleService;

@RequiredArgsConstructor
class RemoveRoleUseCase {

  @NonNull
  private final RoleService roleService;
  @NonNull
  private final AuditingLogger auditingLogger;

  void activate(@NonNull RemoveRoleRequest request) {
    request.preAudit(auditingLogger::log);
    roleService.remove(request.getId());
    request.postAudit(auditingLogger::log);
  }
}
