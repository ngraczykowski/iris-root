package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.feature.FabFeature;
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputIn;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
class FeedingService {

  private final List<FabFeature> features;
  private final AgentInputServiceClient agentInputServiceClient;

  FeedingService(
      List<FabFeature> features,
      AgentInputServiceClient agentInputServiceClient) {
    if (features.isEmpty()) {
      throw new IllegalStateException("There are no features enabled.");
    }
    this.features = features;
    this.agentInputServiceClient = agentInputServiceClient;
  }

  void createFeatureInputs(FeatureInputsCommand featureInputsCommand) {
    List<AgentInputIn<Feature>> agentInputs = features.stream()
        .flatMap(feature -> feature.createFeatureInput(featureInputsCommand).stream())
        .collect(toList());

    feedUds(agentInputs);
  }

  void feedUds(List<AgentInputIn<Feature>> agentInputs) {
    var batchCreateAgentInputsIn = BatchCreateAgentInputsIn.builder()
        .agentInputs(agentInputs)
        .build();

    var agentInputsOut =
        agentInputServiceClient.createBatchCreateAgentInputs(batchCreateAgentInputsIn);

    log.info("Created agent inputs: {}", agentInputsOut.getCreatedAgentInputs());
  }
}
