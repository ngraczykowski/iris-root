package com.silenteight.hsbc.datasource.feature;

import com.silenteight.hsbc.bridge.match.MatchRawData;

public interface FeatureValuesRetriever<F> {

  F retrieve(MatchRawData matchRawData);
  Feature getFeature();

  default String getFeatureName() {
    return getFeature().getName();
  }
}
