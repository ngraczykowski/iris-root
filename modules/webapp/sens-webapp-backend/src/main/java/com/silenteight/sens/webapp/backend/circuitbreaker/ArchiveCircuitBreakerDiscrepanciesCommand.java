package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
class ArchiveCircuitBreakerDiscrepanciesCommand {

  @NonNull
  List<Long> ids;
}
