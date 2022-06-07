package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.protobuf.ProtoMessageToObjectNodeConverter;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.solving.application.process.port.AgentResponsePort;
import com.silenteight.adjudication.engine.solving.application.publisher.dto.MatchSolutionRequest;
import com.silenteight.adjudication.engine.solving.application.publisher.port.ReadyMatchFeatureVectorPort;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.FeatureSolution;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;
import com.silenteight.sep.base.aspects.metrics.Timed;

import com.google.protobuf.Struct;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class AgentResponseProcess implements AgentResponsePort {

  private final ReadyMatchFeatureVectorPort governanceProvider;
  private final AlertSolvingRepository alertSolvingRepository;
  private final ProtoMessageToObjectNodeConverter converter;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void processMatchesFeatureValue(final AgentExchangeResponse agentResponse) {

    for (AgentOutput agentOutput : agentResponse.getAgentOutputsList()) {

      log.info(
          "Parsing agent response: match: {} for features {}", agentOutput.getMatch(),
          agentOutput.getFeaturesCount());
      String matchName = agentOutput.getMatch();
      long alertId = ResourceName.create(matchName).getLong("alerts");
      long matchId = ResourceName.create(matchName).getLong("matches");

      final var alertSolvingModel = updateMatchFeatureValues(agentOutput, matchId, alertId);
      if (alertSolvingModel.isEmpty() || !alertSolvingModel.isMatchReadyForSolving(matchId)) {
        log.warn("Match is not ready for solving {}", matchId);
        continue;
      }
      MatchSolutionRequest matchSolutionRequest = new MatchSolutionRequest(
          alertId, matchId, alertSolvingModel.getPolicy(),
          alertSolvingModel.getMatchFeatureNames(matchId),
          alertSolvingModel.getMatchFeatureVectors(matchId));
      // TODO it may happen that two requests could be send to governance
      this.governanceProvider.send(matchSolutionRequest);
    }
  }

  private AlertSolving updateMatchFeatureValues(
      AgentOutput agentOutput, long matchId, long alertId) {

    List<Feature> featuresList = agentOutput.getFeaturesList();
    List<FeatureSolution> featureSolutions = createFeatureSolutions(featuresList);

    return alertSolvingRepository.updateMatchFeatureValue(alertId, matchId, featureSolutions);
  }

  private List<FeatureSolution> createFeatureSolutions(List<Feature> featuresList) {
    return featuresList.stream()
        .map(feature -> new FeatureSolution(
            feature.getFeature(),
            feature.getFeatureSolution().getSolution(),
            createReason(feature.getFeatureSolution().getReason())))
        .collect(Collectors.toList());
  }

  private String createReason(Struct reason) {
    return converter.convertToJsonString(reason).orElse("");
  }
}
