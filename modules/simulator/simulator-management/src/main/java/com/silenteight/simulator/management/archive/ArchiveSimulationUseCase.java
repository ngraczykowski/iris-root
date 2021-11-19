package com.silenteight.simulator.management.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.management.domain.SimulationService;

@RequiredArgsConstructor
public class ArchiveSimulationUseCase {

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(ArchiveSimulationRequest request) {
    request.preAudit(auditingLogger::log);
    simulationService.archive(request.getId());
    request.postAudit(auditingLogger::log);
  }
}
