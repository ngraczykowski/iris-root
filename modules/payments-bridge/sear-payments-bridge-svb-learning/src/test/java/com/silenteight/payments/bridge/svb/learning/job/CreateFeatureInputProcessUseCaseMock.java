package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;
import com.silenteight.payments.bridge.datasource.agent.port.CreateFeatureInputProcessUseCase;

import java.util.List;

class CreateFeatureInputProcessUseCaseMock implements CreateFeatureInputProcessUseCase {

  @Override
  public void createStructuredFeatureInputs(
      List<FeatureInputStructured> featureInputsStructured) {

  }

  @Override
  public void createStructuredFeatureInputs(
      List<FeatureInputStructured> featureInputsStructured,
      FeatureInputSpecification featureInputSpecification) {

  }

  @Override
  public void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured) {

  }

  @Override
  public void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured,
      FeatureInputSpecification featureInputSpecification) {

  }
}
