package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.datasource.datamodel.MatchData;

public interface FeatureValuesRetriever<F> {

  F retrieve(MatchData matchData);
  Feature getFeature();

  default String getFeatureName() {
    return getFeature().getName();
  }
}
