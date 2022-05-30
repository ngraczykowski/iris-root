package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;

import java.time.Instant;

public interface DataRetentionJobRepository {

  long save(Instant alertsExpirationDate, DataRetentionMode mode);
}
