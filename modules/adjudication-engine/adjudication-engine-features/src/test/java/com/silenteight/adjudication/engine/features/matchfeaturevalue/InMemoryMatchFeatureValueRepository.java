package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryMatchFeatureValueRepository implements MatchFeatureValueRepository {

  private Map<MatchFeatureValueKey, MatchFeatureValue> store = new ConcurrentHashMap<>();

  @Override
  public MatchFeatureValue save(MatchFeatureValue singleValue) {
    store.put(singleValue.getId(), singleValue);
    return singleValue;
  }

  @Override
  public void saveAll(Iterable<MatchFeatureValue> values) {
    values.forEach(this::save);
  }

  boolean isEmpty() {
    return store.isEmpty();
  }

  MatchFeatureValue getById(long matchId, long agentConfigFeatureId) {
    return store.get(new MatchFeatureValueKey(matchId, agentConfigFeatureId));
  }
}
