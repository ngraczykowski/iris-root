package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.CategoryValue;
import com.silenteight.adjudication.engine.solving.domain.FeatureSolution;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryAlertSolvingRepositoryMock implements AlertSolvingRepository {

  private Map<Long, AlertSolving> alerts = new HashMap<>();

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
