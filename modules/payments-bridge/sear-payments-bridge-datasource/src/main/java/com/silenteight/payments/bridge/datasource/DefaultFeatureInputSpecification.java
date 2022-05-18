package com.silenteight.payments.bridge.datasource;

public enum DefaultFeatureInputSpecification implements FeatureInputSpecification {

  INSTANCE;

  @Override
  public boolean isSatisfy(String featureName) {
    return true;
  }

}
