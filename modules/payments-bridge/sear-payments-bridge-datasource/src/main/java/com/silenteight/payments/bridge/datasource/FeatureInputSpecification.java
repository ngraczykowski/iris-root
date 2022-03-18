package com.silenteight.payments.bridge.datasource;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.categories.api.v2.CategoryValue;

public interface FeatureInputSpecification {

  boolean isSatisfy(
      final AgentInput agentInput);

  boolean isSatisfy(final CategoryValue categoryValue);
}
