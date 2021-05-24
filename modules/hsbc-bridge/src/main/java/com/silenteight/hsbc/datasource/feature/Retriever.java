package com.silenteight.hsbc.datasource.feature;

public interface Retriever {

  Feature getFeature();

  default String getFeatureName() {
    return getFeature().getName();
  }
}
