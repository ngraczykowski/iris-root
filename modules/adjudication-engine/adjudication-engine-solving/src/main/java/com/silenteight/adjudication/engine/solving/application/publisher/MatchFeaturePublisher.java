/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchFeaturePublisherPort;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureStoreDataAccess;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchFeaturePublisher implements MatchFeaturePublisherPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final MatchFeatureStoreDataAccess matchFeatureStoreDataAccess;


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
      inMemorySolvingExecutor.execute(() -> matchFeatureStoreDataAccess.store(matchFeature));
    }
  }
}
