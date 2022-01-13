package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Service
@Qualifier("nameMatchedText")
@RequiredArgsConstructor
class NameMatchedTextFeatureExtractorService implements FeatureExtractor {

  private static final String NAME_TEXT_FEATURE = "nameMatchedText";

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  public FeatureInput createFeatureInputs(EtlHit etlHit) {
    var nameAgentUseCaseRequest = createNameAgentUseCaseRequest(etlHit);
    var nameFeatureInput = createNameFeatureInputUseCase.create(nameAgentUseCaseRequest);
    return createFeatureInput(NAME_TEXT_FEATURE, nameFeatureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(EtlHit etlHit) {
    return NameAgentRequest.builder()
        .feature(NAME_TEXT_FEATURE)
        .watchlistNames(List.of(etlHit.getNameMatchedTexts()))
        .alertedPartyNames(etlHit.getMatchedNames())
        .watchlistType(etlHit.getWatchlistType())
        .matchingTexts(etlHit.getMatchingTexts())
        .build();
  }
}
