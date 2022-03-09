package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.categories.api.v2.CategoryValue;

public interface FeatureInputSpecification {

  boolean isSatisfy(
      final AgentInput agentInput);

  boolean isSatisfy(final CategoryValue categoryValue);
}
