package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.model.Match;

import java.util.List;

public interface MatchRepository {

  void updateStatusByBatchIdAndMatchIdInAndExternalAlertIdIn(
      Match.Status status, String batchId, List<String> matchIds, List<String> externalAlertIds);
}
