package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.feature.FabFeature;
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.agentinput.v1.AgentInputServiceClient;
import com.silenteight.universaldatasource.api.library.agentinput.v1.BatchCreateAgentInputsIn;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class FeedingService {

  private final List<FabFeature<? extends Feature>> features;
  private final AgentInputServiceClient agentInputServiceClient;

  FeedingService(
      List<FabFeature<? extends Feature>> features,
      AgentInputServiceClient agentInputServiceClient) {
    if (features.isEmpty()) {
      throw new IllegalStateException("There are no features enabled.");
    }
    this.features = features;
    this.agentInputServiceClient = agentInputServiceClient;
  }

  void createFeatureInputs(FeatureInputsCommand featureInputsCommand) {
    features.stream()
        .map(feature -> feature.createFeatureInput(featureInputsCommand))
        .forEach(this::feedUds);
  }

  void feedUds(BatchCreateAgentInputsIn<? extends Feature> batchCreateAgentInputsIn) {
    var agentInputsOut =
        agentInputServiceClient.createBatchCreateAgentInputs(batchCreateAgentInputsIn);

    log.info("Created agent inputs: {}", agentInputsOut.getCreatedAgentInputs());
  }
}
