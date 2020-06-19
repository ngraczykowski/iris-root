package com.silenteight.sens.webapp.backend.circuitbreaker;

import com.silenteight.proto.serp.v1.circuitbreaker.command.ArchiveDiscrepanciesCommand;

public interface CircuitBreakerMessageGateway {

  void send(ArchiveDiscrepanciesCommand command);
}
