package com.silenteight.adjudication.engine.analysis.agentresponse;

import java.util.Map;
import java.util.UUID;

public interface FeatureIdsProvider {

  Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId);
}
