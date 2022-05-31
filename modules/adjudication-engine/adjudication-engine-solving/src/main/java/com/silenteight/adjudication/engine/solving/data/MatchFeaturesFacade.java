package com.silenteight.adjudication.engine.solving.data;

import java.util.Map;
import java.util.Set;

public interface MatchFeaturesFacade {

  Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts);
}
