package com.silenteight.payments.bridge.svb.learning.features.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Service
@Qualifier("name")
@RequiredArgsConstructor
class NameFeatureExtractor implements FeatureExtractor {


  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  public List<FeatureInput> createFeatureInputs(LearningMatch learningMatch) {
    var nameAgentUseCaseRequest = createNameAgentUseCaseRequest(learningMatch);
    var nameFeatureInput = createNameFeatureInputUseCase.create(nameAgentUseCaseRequest);
    var featureInput = createFeatureInput(NAME_FEATURE, nameFeatureInput);
    return List.of(featureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(LearningMatch learningMatch) {
    return NameAgentRequest.builder()
        .feature(NAME_FEATURE)
        .watchlistNames(learningMatch.getWatchlistNames())
        .alertedPartyNames(learningMatch.getAlertedPartyNames())
        .watchlistType(learningMatch.getWatchlistType())
        .matchingTexts(learningMatch.getMatchingTexts())
        .build();
  }
}
