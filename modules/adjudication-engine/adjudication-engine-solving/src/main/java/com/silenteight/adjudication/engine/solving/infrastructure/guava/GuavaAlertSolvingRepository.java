/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.infrastructure.guava;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.CategoryValue;
import com.silenteight.adjudication.engine.solving.domain.FeatureSolution;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.google.common.cache.Cache;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
class GuavaAlertSolvingRepository implements AlertSolvingRepository {
  private final Cache<Long, AlertSolving> cache;
  private static final ReentrantLock lock = new ReentrantLock();

  @Override
  @Timed(
      percentiles = {0.5, 0.95, 0.99},
      histogram = true)
  public AlertSolving get(Long id) {
    log.debug("[GuavaSolving] Getting AlertSolvingModel id:{}", id);
    return this.executeInLock(
        () -> {
          var alertSolving = cache.getIfPresent(id);
          return alertSolving != null ? alertSolving : AlertSolving.empty();
        });
  }

  @Override
  @Timed(
      percentiles = {0.5, 0.95, 0.99},
      histogram = true)
  public AlertSolving save(final AlertSolving alertSolvingModel) {
    final long key = alertSolvingModel.id();
    log.debug("[GuavaSolving] Adding AlertSolvingModel to key:{}", key);

    return this.executeInLock(
        () -> {
          this.cache.put(key, alertSolvingModel);
          return alertSolvingModel;
        });
  }

  public AlertSolving updateMatchFeatureValue(
      long alertId, long matchId, List<FeatureSolution> featureSolutions) {
    return this.executeInLock(
        () -> {
          var alertSolving = cache.getIfPresent(alertId);
          if (alertSolving == null) {
            throw new AlertSolvingNotPresentException(alertId);
          }
          log.debug(
              "[GuavaSolving] Updating features matchId:{}, foeatures: {}",
              matchId,
              featureSolutions);
          return alertSolving.updateMatchFeatureValues(matchId, featureSolutions);
        });
  }

  public AlertSolving updateMatchCategoryValue(
      long alertId, long matchId, CategoryValue categoryValues) {
    return this.executeInLock(
        () -> {
          var alertSolving = cache.getIfPresent(alertId);
          if (alertSolving == null) {
            throw new AlertSolvingNotPresentException(alertId);
          }
          log.debug(
              "[GuavaSolving] Updating categories matchId:{}, category values: {}",
              matchId,
              categoryValues);
          return alertSolving.updateMatchCategoryValues(matchId, List.of(categoryValues));
        });
  }

  @Override
  public AlertSolving updateMatchSolution(
      long alertId, long matchId, String matchSolution, String reason) {
    return this.executeInLock(
        () -> {
          var alertSolving = cache.getIfPresent(alertId);
          if (alertSolving == null) {
            throw new AlertSolvingNotPresentException(alertId);
          }
          log.debug(
              "[GuavaSolving] Updating match solution matchId:{}, solution: {}",
              matchId,
              matchSolution);
          return alertSolving.updateMatchSolution(matchId, matchSolution, reason);
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

  class AlertSolvingNotPresentException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final long alertId;

    public AlertSolvingNotPresentException(long alertId) {
      this.alertId = alertId;
    }
  }
}
