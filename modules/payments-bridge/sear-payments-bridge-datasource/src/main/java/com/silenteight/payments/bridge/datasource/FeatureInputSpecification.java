package com.silenteight.payments.bridge.datasource;

public interface FeatureInputSpecification {

  boolean isSatisfy(final String featureName);
}
