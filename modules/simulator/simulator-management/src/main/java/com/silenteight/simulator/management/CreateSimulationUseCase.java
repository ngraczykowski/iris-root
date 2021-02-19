package com.silenteight.simulator.management;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.auditing.bs.AuditingLogger;

@AllArgsConstructor
public class CreateSimulationUseCase {

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(CreateSimulationRequest request) {
    request.preAudit(auditingLogger::log);
    simulationService.createSimulation(request);
    request.postAudit(auditingLogger::log);
  }
}
