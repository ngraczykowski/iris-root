package com.silenteight.adjudication.engine.solving.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolvingModel;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.hazelcast.map.IMap;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
// TODO change to strong typing after model will be defined
class InMemoryAlertStorageProvider implements StorageProvider {

  private final IMap<String, AlertSolvingModel> map;
  // TODO get it from property
  private static long maxAlertProcessingSeconds = 20;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void put(String key, AlertSolvingModel model) {
    log.debug("Adding AlertSolvingModel to key:{}", key);
    map.set(key, model, maxAlertProcessingSeconds, TimeUnit.SECONDS);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public AlertSolvingModel get(String key) {
    log.debug("Getting AlertSolvingModel key:{}", key);
    return map.getOrDefault(key, AlertSolvingModel.empty());
  }

}
