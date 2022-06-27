package com.silenteight.simulator.management.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.management.domain.SimulationService;

@RequiredArgsConstructor
public class CancelSimulationUseCase {

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CancelSimulationRequest request) {
    request.preAudit(auditingLogger::log);
    simulationService.cancel(request.getId());
    request.postAudit(auditingLogger::log);
  }
}
