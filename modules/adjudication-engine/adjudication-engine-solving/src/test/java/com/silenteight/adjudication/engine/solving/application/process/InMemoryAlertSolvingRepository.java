package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.FeatureSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryAlertSolvingRepository implements AlertSolvingRepository {

  private final Map<Long, AlertSolving> alertSolvings = new HashMap<>();

  @Override
  public AlertSolving get(Long key) {
    return alertSolvings.get(key);
  }

  @Override
  public AlertSolving save(AlertSolving model) {
    alertSolvings.put(model.id(), model);

    return model;
  }

  @Override
  public AlertSolving updateMatchFeatureValue(
      long alertId, long matchId, List<FeatureSolution> featureSolutions) {
    return AlertSolving.empty();
  }

  @Override
  public AlertSolving updateMatchSolution(
      long alertId, long matchId, String matchSolution, String reason) {
    return AlertSolving.empty();
  }
}
