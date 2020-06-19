package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CircuitBreakerIntegrationChannels {

  public static final String ARCHIVE_DISCREPANCIES_OUTBOUND_CHANNEL =
      "archiveDiscrepanciesOutboundChannel";
}
