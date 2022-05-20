package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.FeatureSolution;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.hazelcast.map.IMap;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
// TODO change to strong typing after model will be defined
class InMemoryAlertSolvingRepository implements AlertSolvingRepository {

  private final EventStore eventStore;
  private final IMap<Long, AlertSolving> map;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public AlertSolving get(Long id) {
    log.debug("Getting AlertSolvingModel id:{}", id);
    return this.executeInLock(id, () -> map.getOrDefault(id, AlertSolving.empty()));
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public AlertSolving save(final AlertSolving alertSolvingModel) {
    final long key = alertSolvingModel.id();
    log.debug("Adding AlertSolvingModel to key:{}", key);

    return this.executeInLock(key, () -> {
      this.map.set(key, alertSolvingModel);
      //      this.eventStore.publish(alertSolvingModel.pendingEvents());
      //      alertSolvingModel.clear();
      return alertSolvingModel;
    });
  }

  public AlertSolving updateMatchFeatureValue(
      long alertId, long matchId, List<FeatureSolution> featureSolutions) {
    return this.map.executeOnKey(alertId, new AlertSolvingProcessor() {
      @Override
      public AlertSolving process(Entry<Long, AlertSolving> entry) {
        log.debug("Updating features matchId:{}, foeatures: {}", matchId, featureSolutions);
        entry.setValue(entry.getValue().updateMatchFeatureValues(matchId, featureSolutions));
        return entry.getValue();
      }
    });
  }

  @Override
  public AlertSolving updateMatchSolution(
      long alertId, long matchId, String matchSolution, String reason) {
    return this.map.executeOnKey(alertId, new AlertSolvingProcessor() {
      @Override
      public AlertSolving process(Entry<Long, AlertSolving> entry) {
        log.debug("Updating match solution matchId:{}, solution: {}", matchId, matchSolution);
        entry.setValue(entry.getValue().updateMatchSolution(matchId, matchSolution, reason));
        return entry.getValue();
      }
    });
  }

  private <T> T executeInLock(final Long key, final Supplier<T> action) {

    if (!this.map.tryLock(key)) {
      throw new LockAlertSolvingKeyException(key);
    }

    try {
      return action.get();
    } finally {
      if (this.map.isLocked(key)) {
        this.map.forceUnlock(key);
      }
    }
  }

  private static final class LockAlertSolvingKeyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    LockAlertSolvingKeyException(Long key) {
      super("Could lock key : " + key);
    }
  }

}
