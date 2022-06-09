package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.CategoryValue;
import com.silenteight.adjudication.engine.solving.domain.FeatureSolution;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class InMemoryAlertSolvingRepositoryMock implements AlertSolvingRepository {

  private Map<Long, AlertSolving> alerts = new HashMap<>();

  public InMemoryAlertSolvingRepositoryMock(
      InMemoryMatchFeatureDataAccess inMemoryMatchFeatureDataAccess) {
    var alerts =
        inMemoryMatchFeatureDataAccess.findAnalysisFeatures(Set.of(1L), Set.of(1L, 2L, 3L, 4L));
    alerts.values().stream().map(AlertSolving::new).forEach(this::save);
  }

  public InMemoryAlertSolvingRepositoryMock() {

  }

  @Override
  public AlertSolving get(@NotNull Long key) {
    synchronized (InMemoryAlertSolvingRepositoryMock.class) {
      return alerts.get(key);
    }
  }

  @Override
  public AlertSolving save(@NotNull AlertSolving model) {
    synchronized (InMemoryAlertSolvingRepositoryMock.class) {
      alerts.putIfAbsent(model.getAlertId(), model);
      return model;
    }
  }

  @Override
  public AlertSolving updateMatchFeatureValue(
      long alertId, long matchId, List<FeatureSolution> featureSolutions) {
    synchronized (InMemoryAlertSolvingRepositoryMock.class) {
      featureSolutions.forEach(
          fs -> alerts.get(alertId).updateMatchSolution(matchId, fs.getSolution(), fs.getReason()));
      return alerts.get(alertId);
    }
  }

  @Override
  public AlertSolving updateMatchSolution(
      long alertId, long matchId, @NotNull String matchSolution, @NotNull String reason) {
    synchronized (InMemoryAlertSolvingRepositoryMock.class) {
      return alerts.get(alertId).updateMatchSolution(matchId, matchSolution, reason);
    }
  }

  @Override
  public AlertSolving updateMatchCategoryValue(
      long alertId, long matchId, @NotNull CategoryValue categoryValues) {
    synchronized (InMemoryAlertSolvingRepositoryMock.class) {
      return alerts.get(alertId).updateMatchCategoryValues(matchId, List.of(categoryValues));
    }
  }
}
