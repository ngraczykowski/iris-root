package com.silenteight.adjudication.engine.solving.domain;

import java.util.Map;

public interface MatchFeaturesRepository {

  MatchFeature get(final MatchFeatureKey key);

  void save(final MatchFeatureKey key, final MatchFeature model);

  void saveAll(Map<MatchFeatureKey, MatchFeature> alertMatchesFeatures);
}
