package com.silenteight.fab.dataprep.domain;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.feature.BuildFeatureCommand;
import com.silenteight.fab.dataprep.domain.feature.FabFeature;
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
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
  private final CategoryService categoryService;

  FeedingService(
      List<FabFeature> features,
      AgentInputServiceClient agentInputServiceClient,
      CategoryService categoryService) {
    if (features.isEmpty()) {
      throw new IllegalStateException("There are no features enabled.");
    }
    this.features = features;
    this.agentInputServiceClient = agentInputServiceClient;
    this.categoryService = categoryService;
  }

  void createFeatureInputs(FeatureInputsCommand featureInputsCommand) {
    RegisteredAlert registeredAlert = featureInputsCommand.getRegisteredAlert();
    List<AgentInputIn<Feature>> agentInputs = registeredAlert.getMatches()
        .stream()
        .map(match ->
            AgentInputIn.builder()
                .match(match.getMatchName())
                .alert(registeredAlert.getAlertName())
                .featureInputs(buildFeatures(registeredAlert, match))
                .build())
        .collect(toList());

    if (agentInputs.isEmpty()) {
      log.debug("AgentInputs list is empty");
    } else {
      feedUds(agentInputs);
    }

    categoryService.createCategoryInputs(featureInputsCommand);
  }

  private List<Feature> buildFeatures(
      RegisteredAlert registeredAlert, RegisteredAlert.Match match) {
    return features.stream()
        .map(feature -> feature.buildFeature(BuildFeatureCommand
            .builder()
            .parsedMessageData(registeredAlert.getParsedMessageData())
            .match(match)
            .build()))
        .collect(toList());
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
