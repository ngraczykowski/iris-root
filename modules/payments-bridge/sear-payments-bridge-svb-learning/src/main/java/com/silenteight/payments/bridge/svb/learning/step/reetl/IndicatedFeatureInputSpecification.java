package com.silenteight.payments.bridge.svb.learning.step.reetl;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.payments.bridge.svb.learning.step.etl.feature.service.FeatureInputSpecification;

import java.util.List;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
class IndicatedFeatureInputSpecification implements FeatureInputSpecification {

  private final List<String> allowedFeaturedInputs;


  /**
   * Check, if feature input should be processed.
   *
   * @return should agent input be processed or not.
   */
  public boolean isSatisfy(@Nonnull final AgentInput agentInput) {
    if (this.allowedFeaturedInputs.isEmpty()) {
      return true;
    }
    return this.allowedFeaturedInputs.contains(agentInput.getName());
  }

  @Override
  public boolean isSatisfy(CategoryValue categoryValue) {
    if (this.allowedFeaturedInputs.isEmpty()) {
      return true;
    }
    return this.allowedFeaturedInputs.contains(categoryValue.getName());
  }

}
