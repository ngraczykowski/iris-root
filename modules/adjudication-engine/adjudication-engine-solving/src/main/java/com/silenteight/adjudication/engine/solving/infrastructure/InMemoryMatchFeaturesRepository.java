package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.MatchFeature;
import com.silenteight.adjudication.engine.solving.domain.MatchFeatureKey;
import com.silenteight.adjudication.engine.solving.domain.MatchFeaturesRepository;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.hazelcast.map.IMap;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
// TODO change to strong typing after model will be defined
class InMemoryMatchFeaturesRepository implements MatchFeaturesRepository {

  private final IMap<MatchFeatureKey, MatchFeature> map;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public MatchFeature get(MatchFeatureKey key) {
    log.debug("Getting MatchFeature alertId:{}", key.getAlertId());
    return this.executeInLock(key, () -> map.getOrDefault(key, MatchFeature.empty()));
  }

  private <T> T executeInLock(final MatchFeatureKey key, final Supplier<T> action) {
    if (this.map.tryLock(key)) {
      try {
        return action.get();
      } finally {
        if (this.map.isLocked(key)) {
          this.map.forceUnlock(key);
        }
      }
    }
    // TODO: return null or throw exception.
    return null;
  }

}
