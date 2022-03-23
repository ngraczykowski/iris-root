package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;

@Service
@Qualifier("name")
@RequiredArgsConstructor
class NameFeatureExtractorService implements FeatureExtractor {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  public FeatureInput createFeatureInputs(EtlHit etlHit) {
    var nameAgentUseCaseRequest = createNameAgentUseCaseRequest(etlHit);
    var nameFeatureInput = createNameFeatureInputUseCase.createDefault(nameAgentUseCaseRequest);
    return createFeatureInput(NAME_FEATURE, nameFeatureInput);
  }

  @Override
  public String name() {
    return getFullFeatureName(NAME_FEATURE);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(EtlHit etlHit) {
    return NameAgentRequest.builder()
        .feature(NAME_FEATURE)
        .watchlistNames(etlHit.getWatchlistNames())
        .alertedPartyNames(etlHit.getAlertedPartyNames())
        .watchlistType(etlHit.getWatchlistType())
        .matchingTexts(etlHit.getMatchingTexts())
        .build();
  }
}
