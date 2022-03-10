package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValue;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityNotFoundException;

class InMemoryMatchFeatureValueDataAccess implements MatchFeatureValueDataAccess {

  private Set<MatchFeatureValue> store = new HashSet<>();

  @Override
  public int saveAll(Iterable<MatchFeatureValue> values) {
    var previousSize = store.size();
    values.forEach(store::add);
    return store.size() - previousSize;
  }

  @Override
  public int delete(Iterable<String> features) {
    var sizeBefore = store.size();
    for (var feature : features) {
      store.removeIf(f -> f.getValue().equals(feature));
    }
    var sizeAfter = store.size();
    return sizeBefore - sizeAfter;
  }

  boolean isEmpty() {
    return store.isEmpty();
  }

  MatchFeatureValue getById(long matchId, long agentConfigFeatureId) {
    return store.stream()
        .filter(
            v -> v.getMatchId() == matchId && v.getAgentConfigFeatureId() == agentConfigFeatureId)
        .findAny()
        .orElseThrow(EntityNotFoundException::new);
  }
}
