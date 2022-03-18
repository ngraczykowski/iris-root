package com.silenteight.payments.bridge.datasource;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;

public enum DefaultFeatureInputSpecification implements FeatureInputSpecification {

  INSTANCE;

  @Override
  public boolean isSatisfy(
      AgentInput agentInput) {
    return true;
  }

  @Override
  public boolean isSatisfy(CategoryValue categoryValue) {
    return true;
  }

}
