package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.datasource.DefaultFeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.FeatureInputSpecification;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;
import com.silenteight.payments.bridge.svb.learning.step.etl.feature.port.CreateFeatureUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class CreateFeatureService implements CreateFeatureUseCase {

  private final List<FeatureExtractor> featureExtractors;
  private final List<UnstructuredFeatureExtractor> unstructuredFeatureExtractors;
  private final CreateAgentInputsClient createAgentInputsClient;

  @Override
  public List<AgentInput> createFeatureInputs(
      List<EtlHit> etlHits, RegisterAlertResponse registeredAlert) {
    var alertName = registeredAlert.getAlertName();

    var agentInputs = etlHits.stream().map(hit -> AgentInput
            .newBuilder()
            .setAlert(alertName)
            .setMatch(registeredAlert.getMatchName(hit.getMatchId()))
            .addAllFeatureInputs(createFeaturesInputs(hit))
            .build())
        .collect(toList());

    createAgentInputsClient.createAgentInputs(
        BatchCreateAgentInputsRequest.newBuilder().addAllAgentInputs(agentInputs).build());

    return createFeatureInputs(etlHits, registeredAlert, DefaultFeatureInputSpecification.INSTANCE);
  }

  @Override
  public List<AgentInput> createFeatureInputs(
      List<EtlHit> etlHits, RegisterAlertResponse registeredAlert,
      FeatureInputSpecification featureInputSpecification) {
    var alertName = registeredAlert.getAlertName();

    var agentInputs = etlHits.stream().map(hit -> AgentInput
            .newBuilder()
            .setAlert(alertName)
            .setMatch(registeredAlert.getMatchName(hit.getMatchId()))
            .addAllFeatureInputs(createFeaturesInputs(hit))
            .build())
        .filter(featureInputSpecification::isSatisfy)
        .collect(toList());

    createAgentInputsClient.createAgentInputs(
        BatchCreateAgentInputsRequest.newBuilder().addAllAgentInputs(agentInputs).build());

    return agentInputs;
  }

  @Override
  public List<AgentInput> createUnstructuredFeatureInputs(
      final List<HitComposite> hitComposites, final RegisterAlertResponse registeredAlert
  ) {
    return createUnstructuredFeatureInputs(
        hitComposites, registeredAlert, DefaultFeatureInputSpecification.INSTANCE);
  }

  @Override
  public List<AgentInput> createUnstructuredFeatureInputs(
      final List<HitComposite> hitComposites,
      final RegisterAlertResponse registeredAlert,
      final FeatureInputSpecification featureInputSpecification
  ) {
    var alertName = registeredAlert.getAlertName();

    var agentInputs = hitComposites.stream().map(hit -> AgentInput
            .newBuilder()
            .setAlert(alertName)
            .setMatch(registeredAlert.getMatchName(hit.getMatchId()))
            .addAllFeatureInputs(createUnstructuredFeaturesInputs(hit))
            .build())
        .filter(featureInputSpecification::isSatisfy)
        .collect(toList());

    final BatchCreateAgentInputsRequest batchCreateAgentInputsRequest =
        BatchCreateAgentInputsRequest.newBuilder().addAllAgentInputs(agentInputs).build();
    createAgentInputsClient.createAgentInputs(batchCreateAgentInputsRequest);
    return agentInputs;
  }


  @Nonnull
  private List<FeatureInput> createFeaturesInputs(EtlHit hit) {
    return featureExtractors
        .stream()
        .map(fe -> fe.createFeatureInputs(hit))
        .collect(toList());
  }

  @Nonnull
  private List<FeatureInput> createUnstructuredFeaturesInputs(HitComposite hitComposite) {
    return unstructuredFeatureExtractors
        .stream()
        .map(fe -> fe.createFeatureInputs(hitComposite))
        .collect(toList());
  }
}
