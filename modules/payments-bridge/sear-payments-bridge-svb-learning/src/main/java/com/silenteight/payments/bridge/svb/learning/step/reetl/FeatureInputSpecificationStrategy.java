package com.silenteight.payments.bridge.svb.learning.step.reetl;

import com.silenteight.payments.bridge.datasource.DefaultFeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.IndicatedFeatureInputSpecification;

import java.util.List;

enum FeatureInputSpecificationStrategy {

  INSTANCE;

  FeatureInputSpecification chooseSpecification(
      final List<String> determinedFeatureInputList
  ) {
    if (determinedFeatureInputList.isEmpty()) {
      return DefaultFeatureInputSpecification.INSTANCE;
    }

    return new IndicatedFeatureInputSpecification(determinedFeatureInputList);
  }
}
