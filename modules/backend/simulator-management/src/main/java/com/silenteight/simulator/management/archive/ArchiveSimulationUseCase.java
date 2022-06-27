package com.silenteight.simulator.management.archive;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.simulator.management.domain.SimulationService;

@Slf4j
@RequiredArgsConstructor
public class ArchiveSimulationUseCase {

  @NonNull
  private final SimulationService simulationService;

  @NonNull
  private final AuditingLogger auditingLogger;

  public void activate(ArchiveSimulationRequest request) {
    log.info("ArchiveSimulationRequest request received, simulationId={}.", request.getId());
    request.preAudit(auditingLogger::log);
    simulationService.archive(request.getId());
    request.postAudit(auditingLogger::log);
    log.debug("ArchiveSimulationRequest request processed.");
  }
}
