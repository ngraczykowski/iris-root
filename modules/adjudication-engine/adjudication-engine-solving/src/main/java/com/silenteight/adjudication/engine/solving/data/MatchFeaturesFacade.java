package com.silenteight.adjudication.engine.solving.data;

import java.util.List;
import java.util.Set;

public interface MatchFeaturesFacade {

  List<MatchFeatureDao> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts);
}
