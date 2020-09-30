package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.circuitbreaker.command.ArchiveDiscrepanciesCommand;
import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;

import java.util.List;

import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CIRCUIT_BREAKER;

@Slf4j
@RequiredArgsConstructor
class ArchiveDiscrepanciesUseCase {

  private final CircuitBreakerMessageGateway gateway;

  private final AuditTracer auditTracer;

  void apply(@NonNull ArchiveCircuitBreakerDiscrepanciesCommand command) {
    log.debug(CIRCUIT_BREAKER, "Archiving of discrepancy requested, command={}", command);

    gateway.send(archiveDiscrepanciesCommandOf(command.getIds()));
    auditTracer.save(new DiscrepancyArchivingRequestedEvent(command));
  }

  private static ArchiveDiscrepanciesCommand archiveDiscrepanciesCommandOf(
      List<Long> discrepancyIds) {

    return ArchiveDiscrepanciesCommand
        .newBuilder()
        .addAllDiscrepancyIds(discrepancyIds)
        .build();
  }
}
