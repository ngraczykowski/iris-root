package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_TEXT_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Service
@Qualifier("nameMatchedText")
@RequiredArgsConstructor
class NameMatchedTextFeatureExtractorService implements UnstructuredFeatureExtractor {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  public FeatureInput createFeatureInputs(HitComposite hitComposite) {
    var nameAgentUseCaseRequest = createNameAgentUseCaseRequest(hitComposite);
    var nameFeatureInput = createNameFeatureInputUseCase.createDefault(nameAgentUseCaseRequest);
    return createFeatureInput(NAME_TEXT_FEATURE, nameFeatureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(HitComposite hitComposite) {
    return NameAgentRequest.builder()
        .feature(NAME_TEXT_FEATURE)
        .watchlistNames(List.of(hitComposite.getFkcoVNameMatchedText()))
        .alertedPartyNames(hitComposite.getMatchedNames())
        .watchlistType(hitComposite.getWatchlistType())
        .matchingTexts(hitComposite.getMatchingTexts())
        .build();
  }
}
