package com.silenteight.bridge.core.registration.domain.port.outgoing;

import java.util.List;

public interface DataRetentionJobAlertRepository {

  void saveAll(long jobId, List<Long> alertPrimaryIds);
}
