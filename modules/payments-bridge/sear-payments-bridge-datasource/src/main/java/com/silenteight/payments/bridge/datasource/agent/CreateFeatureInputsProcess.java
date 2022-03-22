package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.datasource.DefaultFeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateFeatureInputsProcess {

  private final List<FeatureInputStructuredFactory> featureInputStructuredFactories;
  private final List<FeatureInputUnstructuredFactory> featureInputUnstructuredFactories;
  private final FeatureInputRepository repository;

  public void createStructuredFeatureInputs(
      List<FeatureInputStructured> featureInputsStructured) {
    this.createStructuredFeatureInputs(
        featureInputsStructured, DefaultFeatureInputSpecification.INSTANCE);
  }

  public void createStructuredFeatureInputs(
      List<FeatureInputStructured> featureInputsStructured,
      FeatureInputSpecification featureInputSpecification) {

    var agentInputs = featureInputsStructured.stream()
        .map(this::createFeatureInputStructured)
        .flatMap(List::stream)
        .filter(featureInputSpecification::isSatisfy)
        .collect(Collectors.toList());

    saveAgentInputs(agentInputs);
  }

  public void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured) {
    this.createUnstructuredFeatureInputs(
        featureInputsUnstructured, DefaultFeatureInputSpecification.INSTANCE);
  }

  public void createUnstructuredFeatureInputs(
      List<FeatureInputUnstructured> featureInputsUnstructured,
      FeatureInputSpecification featureInputSpecification) {

    var agentInputs = featureInputsUnstructured.stream()
        .map(this::createFeatureInputUnstructured)
        .flatMap(List::stream)
        .filter(featureInputSpecification::isSatisfy)
        .collect(Collectors.toList());

    saveAgentInputs(agentInputs);
  }

  private List<AgentInput> createFeatureInputStructured(
      FeatureInputStructured featureInputStructured) {
    return featureInputStructuredFactories.stream()
        .map(factory -> factory.createAgentInput(featureInputStructured))
        .collect(Collectors.toList());
  }

  private List<AgentInput> createFeatureInputUnstructured(
      FeatureInputUnstructured featureInputUnstructured) {
    return featureInputUnstructuredFactories.stream()
        .map(factory -> factory.createAgentInput(featureInputUnstructured))
        .collect(Collectors.toList());
  }

  private void saveAgentInputs(List<AgentInput> agentInputs) {
    repository.save(agentInputs);
  }

}
