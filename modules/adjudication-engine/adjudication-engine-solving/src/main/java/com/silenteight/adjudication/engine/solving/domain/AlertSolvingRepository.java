package com.silenteight.adjudication.engine.solving.domain;

import java.util.List;

public interface AlertSolvingRepository {

  AlertSolving get(final Long key);

  AlertSolving save(final AlertSolving model);

  AlertSolving updateMatchFeatureValue(
      long alertId, long matchId, List<FeatureSolution> featureSolutions);

  AlertSolving updateMatchSolution(long alertId, long matchId, String matchSolution, String reason);
}
