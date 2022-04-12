package com.silenteight.adjudication.engine.solving.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.hazelcast.map.IMap;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
// TODO change to strong typing after model will be defined
class InMemoryAlertStorageProvider implements AlertSolvingRepository {

  // TODO get it from property
  private static long maxAlertProcessingSeconds = 20;
  private final EventStore eventStore;
  private final IMap<Long, AlertSolving> map;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void save(final AlertSolving alertSolvingModel) {
    log.debug("Adding AlertSolvingModel to key:{}", alertSolvingModel.id());
    map.set(alertSolvingModel.id(), alertSolvingModel, maxAlertProcessingSeconds, TimeUnit.SECONDS);

    this.eventStore.publish(alertSolvingModel.pendingEvents());

    alertSolvingModel.clear();
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public AlertSolving get(Long id) {
    log.debug("Getting AlertSolvingModel id:{}", id);
    return map.getOrDefault(id, AlertSolving.empty());
  }

}
