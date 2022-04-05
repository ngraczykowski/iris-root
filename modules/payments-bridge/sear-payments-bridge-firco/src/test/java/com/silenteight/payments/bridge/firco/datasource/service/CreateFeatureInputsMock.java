package com.silenteight.payments.bridge.firco.datasource.service;

import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;
import com.silenteight.payments.bridge.datasource.agent.port.CreateFeatureInputProcessUseCase;

import java.util.LinkedList;
import java.util.List;

class CreateFeatureInputsMock implements CreateFeatureInputProcessUseCase {

  private List<FeatureInputUnstructured> featureInputUnstructureds = new LinkedList<>();

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
    featureInputUnstructureds.addAll(featureInputsUnstructured);
  }

  @Override
  public void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured,
      FeatureInputSpecification featureInputSpecification) {

  }

  public List<FeatureInputUnstructured> getFeatureInputUnstructureds() {
    return featureInputUnstructureds;
  }
}
