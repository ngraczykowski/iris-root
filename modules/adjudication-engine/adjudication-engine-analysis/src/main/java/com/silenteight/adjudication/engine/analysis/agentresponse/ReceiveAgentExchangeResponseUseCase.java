package com.silenteight.adjudication.engine.analysis.agentresponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.MatchFeatureValueFacade;
import com.silenteight.adjudication.engine.features.matchfeaturevalue.dto.MatchFeatureValueDto;
import com.silenteight.agents.v1.api.exchange.AgentExchangeResponse;
import com.silenteight.agents.v1.api.exchange.AgentOutput;
import com.silenteight.agents.v1.api.exchange.AgentOutput.Feature;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Service
@Slf4j
class ReceiveAgentExchangeResponseUseCase {

  private final MatchFeatureValueFacade matchFeatureValueFacade;
  private final FeatureIdsProvider featureIdsProvider;

  void receiveAgentExchangeResponse(UUID agentExchangeRequestId, AgentExchangeResponse response) {
    log.debug("Receiving agent exchange response: requestId={}, agentOutputCount={}",
        agentExchangeRequestId, response.getAgentOutputsCount());

    if (response.getAgentOutputsCount() == 0) {
      log.warn("Received empty agent exchange response, ignoring: requestId={}",
          agentExchangeRequestId);
      return;
    }

    var featureIds = featureIdsProvider.getFeatureToIdsMap(agentExchangeRequestId);

    if (featureIds.isEmpty()) {
      log.warn("Received agent exchange response for nonexistent request, ignoring: requestId={}",
          agentExchangeRequestId);
      return;
    }

    var dtos = new MatchFeatureValueMapper(featureIds)
        .mapValues(response.getAgentOutputsList())
        .collect(toList());

    log.info("Received agent exchange response: requestId={}, featureValueCount={}, features={}",
        agentExchangeRequestId, dtos.size(), featureIds.keySet());

    matchFeatureValueFacade.createMatchFeatureValues(dtos);
  }

  @RequiredArgsConstructor
  private static final class MatchFeatureValueMapper {

    private final Map<String, Long> featureToId;

    Stream<MatchFeatureValueDto> mapValues(Collection<AgentOutput> outputs) {
      // NOTE(ahaczewski): It is possible to consume only the requested features, instead of
      //  failing. To do so, filter the features list with:
      //
      //    .filter(feature -> featureToId.containsKey(feature.getFeature()))
      //
      //  in mapFeatures() method.
      validateAllFeaturesAvailable(outputs);

      return outputs.stream()
          .flatMap(output -> mapFeatures(output.getMatch(), output.getFeaturesList()));
    }

    private void validateAllFeaturesAvailable(Collection<AgentOutput> outputs) {
      var receivedFeatures = outputs
          .stream()
          .map(AgentOutput::getFeaturesList)
          .flatMap(List::stream)
          .map(Feature::getFeature)
          .collect(toSet());

      var expectedFeatures = featureToId.keySet();

      receivedFeatures.removeAll(expectedFeatures);

      if (!receivedFeatures.isEmpty()) {
        throw new UnexpectedFeaturesReceivedException(receivedFeatures);
      }
    }

    private Stream<? extends MatchFeatureValueDto> mapFeatures(
        String match, List<Feature> featuresList) {

      return featuresList.stream()
          .map(feature -> MatchFeatureValueDto.builder()
              .agentConfigFeatureId(featureToId.get(feature.getFeature()))
              .matchId(ResourceName.create(match).getLong("matches"))
              .value(feature.getFeatureSolution().getSolution())
              .reason(feature.getFeatureSolution().getReason())
              .build());
    }
  }
}
