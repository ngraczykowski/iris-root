package com.silenteight.adjudication.engine.analysis.agentresponse;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakeFeatureIdsProvider implements FeatureIdsProvider {

  private final Map<String, Long> featureIds = new HashMap<>();

  @Override
  public Map<String, Long> getFeatureToIdsMap(UUID agentExchangeRequestId) {
    return featureIds;
  }

  void put(String key, long value) {
    featureIds.put(key, value);
  }
}
