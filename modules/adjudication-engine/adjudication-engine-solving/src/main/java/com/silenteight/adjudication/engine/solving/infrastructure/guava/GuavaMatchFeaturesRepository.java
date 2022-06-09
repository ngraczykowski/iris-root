/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.guava;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.MatchFeature;
import com.silenteight.adjudication.engine.solving.domain.MatchFeatureKey;
import com.silenteight.adjudication.engine.solving.domain.MatchFeaturesRepository;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.google.common.cache.Cache;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
// TODO change to strong typing after model will be defined
class GuavaMatchFeaturesRepository implements MatchFeaturesRepository {

  private final Cache<MatchFeatureKey, MatchFeature> cache;
  private static final ReentrantLock lock = new ReentrantLock();

  @Override
  @Timed(
      percentiles = {0.5, 0.95, 0.99},
      histogram = true)
  public MatchFeature get(MatchFeatureKey key) {
    log.debug("Getting MatchFeature alertId:{}", key.getAlertId());
    return this.executeInLock(
        () -> {
          var matchFeature = cache.getIfPresent(key);
          return matchFeature != null ? matchFeature : MatchFeature.empty();
        });
  }

  private <T> T executeInLock(final Supplier<T> action) {
    lock.lock();
    try {
      return action.get();
    } finally {
      lock.unlock();
    }
  }
}
