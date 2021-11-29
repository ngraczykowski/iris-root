package com.silenteight.universaldatasource.app.feature.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;
import com.silenteight.universaldatasource.app.feature.port.incoming.DeleteFeaturesUseCase;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class DeleteFeaturesService implements DeleteFeaturesUseCase {

  private final FeatureDataAccess dataAccess;

  @Timed(value = "uds.feature.use_cases", extraTags = { "action", "deleteFeatures" })
  @Override
  public void delete(List<String> alerts) {

    log.info("Deleting feature inputs: alertCount={}, alerts={}", alerts.size(), alerts);

    int deletedCount = dataAccess.delete(alerts);

    log.info("Feature inputs removed, count={}", deletedCount);
  }
}
