/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchFeaturePublisherPort;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.MatchFeatureValue;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class MatchFeaturePublisher implements MatchFeaturePublisherPort {

  private final Queue<MatchFeatureValue> matchFeaturesQueue;
  private final MatchFeatureStoreDataAccess matchFeatureStoreDataAccess;

  MatchFeaturePublisher(
      final MatchFeatureStoreDataAccess matchFeatureStoreDataAccess,
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<MatchFeatureValue> alertCommentsInputQueue) {
    this.matchFeatureStoreDataAccess = matchFeatureStoreDataAccess;
    this.matchFeaturesQueue = alertCommentsInputQueue;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 500, TimeUnit.MILLISECONDS);
  }

  private void process() {
    try {
      execute();
    } catch (Exception e) {
      log.error("Processing of match feature value  failed: ", e);
    }
  }

  private void execute() {
    log.trace("Start process match feature value ");
    while (true) {
      var matchFeature = this.matchFeaturesQueue.poll();
      if (matchFeature == null) {
        break;
      }
      matchFeatureStoreDataAccess.store(matchFeature);
    }
  }

  @Override
  public void resolve(AlertSolving alertSolving, Long matchId, List<Feature> features) {
    for (var feature : features) {
      var featureName = feature.getFeature();
      var matchFeature = alertSolving.getMatchFeatureValue(matchId, featureName);
      log.debug(
          "Store match feature matchId: {} feature: {} solution: {} applied to alertSolving: {}",
          matchId,
          featureName,
          matchFeature.solution(),
          alertSolving.getAlertId());
      this.matchFeaturesQueue.add(matchFeature);
    }
  }
}
