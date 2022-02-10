package com.silenteight.payments.bridge.svb.learning.features.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class HistoricalRiskAssessmentExtractor implements FeatureExtractor {

  private final List<HistoricalRiskAssessmentFeatureExtractor> features;
  private final HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureAgent;

  @Override
  public List<FeatureInput> createFeatureInputs(LearningMatch learningMatch) {
    return features.stream()
        .map(feature -> createFeatureInput(learningMatch, feature))
        .collect(toList());
  }

  private FeatureInput createFeatureInput(
      LearningMatch learningMatch, HistoricalRiskAssessmentFeatureExtractor feature) {

    var historicalDecisionsFeatureInput =
        getHistoricalDecisionsFeatureInput(learningMatch, feature);

    return AgentDataSourceUtils.createFeatureInput(
        feature.getFeature(), historicalDecisionsFeatureInput);
  }

  private HistoricalDecisionsFeatureInput getHistoricalDecisionsFeatureInput(
      LearningMatch learningMatch, HistoricalRiskAssessmentFeatureExtractor feature) {

    var request =
        createHistoricalRiskAgentRequest(learningMatch, feature);
    return historicalRiskAssessmentFeatureAgent.invoke(request);
  }

  private static HistoricalRiskAssessmentAgentFeatureRequest createHistoricalRiskAgentRequest(
      LearningMatch learningMatch, HistoricalRiskAssessmentFeatureExtractor feature) {

    var featureName = feature.getFeature();
    var matchId = learningMatch.getMatchId();
    var alertedPartyId = feature.getAlertedPartyId(learningMatch.getAlertedPartyData());
    var ofacId = learningMatch.getOfacId();
    var watchlistType = learningMatch.getWatchlistType().toString();
    var discriminator = feature.getDiscriminator();

    return HistoricalRiskAssessmentAgentFeatureRequest.builder()
        .feature(featureName)
        .matchId(matchId)
        .alertedPartyId(alertedPartyId)
        .ofacId(ofacId)
        .watchlistType(watchlistType)
        .discriminator(discriminator)
        .build();
  }
}
