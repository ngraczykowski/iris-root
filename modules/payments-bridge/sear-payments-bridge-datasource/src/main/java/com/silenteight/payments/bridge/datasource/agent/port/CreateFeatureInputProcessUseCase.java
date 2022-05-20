package com.silenteight.payments.bridge.datasource.agent.port;

import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;

import java.util.List;

public interface CreateFeatureInputProcessUseCase {

  void createStructuredFeatureInputs(
      List<FeatureInputStructured> featureInputsStructured);

  void createStructuredFeatureInputs(
      List<FeatureInputStructured> featureInputsStructured,
      FeatureInputSpecification featureInputSpecification);

  void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured);

  public void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured,
      FeatureInputSpecification featureInputSpecification);
}
