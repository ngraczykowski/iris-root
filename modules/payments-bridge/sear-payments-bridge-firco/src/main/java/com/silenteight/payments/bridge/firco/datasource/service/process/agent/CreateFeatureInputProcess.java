package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class CreateFeatureInputProcess implements CreateFeatureInput {

  private final List<CreateFeatureInputStructured> createFeatureInputStructured;
  private final List<CreateFeatureInputUnstructured> createFeatureInputUnstructured;

  @Override
  public List<AgentInput> createStructuredFeatureInputs(AeAlert alert, List<HitData> hitsData) {
    return createFeatureInputStructured.stream()
        .map(process -> process.createFeatureInputs(alert, hitsData))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<AgentInput> createUnstructuredFeatureInputs(
      DatasourceUnstructuredModel featureInputModel) {
    return createFeatureInputUnstructured.stream()
        .map(process -> process.createFeatureInputs(featureInputModel))
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }
}
