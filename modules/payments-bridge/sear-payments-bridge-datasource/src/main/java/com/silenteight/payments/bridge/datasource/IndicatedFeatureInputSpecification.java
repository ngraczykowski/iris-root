package com.silenteight.payments.bridge.datasource;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class IndicatedFeatureInputSpecification implements FeatureInputSpecification {

  private final List<String> allowedFeaturedInputs;

  @Override
  public boolean isSatisfy(final String featureName) {
    if (this.allowedFeaturedInputs.isEmpty()) {
      return true;
    }
    return this.allowedFeaturedInputs.contains(featureName);
  }
}
