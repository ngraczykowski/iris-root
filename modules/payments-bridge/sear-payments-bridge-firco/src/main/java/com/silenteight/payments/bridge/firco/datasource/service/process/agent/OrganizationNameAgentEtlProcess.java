package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.ORGANIZATION_NAME_FEATURE;

@Component
@RequiredArgsConstructor
class OrganizationNameAgentEtlProcess extends BaseAgentEtlProcess<NameFeatureInput> {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    var featureInput = FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(ORGANIZATION_NAME_FEATURE))
        .setAgentFeatureInput(Any.pack(generateNameFeatureInput(hitData)))
        .build();

    return List.of(featureInput);
  }

  private NameFeatureInput generateNameFeatureInput(HitData hitData) {

    var watchlistType = hitData.getHitAndWlPartyData().getWatchlistType();
    var alertedPartyNames = getAlertedPartyNames(hitData);
    var watchlistPartyNames = getWatchlistPartyNames(hitData);

    var nameAgentRequest = NameAgentRequest.builder()
        .feature(ORGANIZATION_NAME_FEATURE)
        .alertedPartyNames(alertedPartyNames)
        .watchlistNames(List.of(watchlistPartyNames))
        .watchlistType(watchlistType)
        .build();

    return createNameFeatureInputUseCase.createForOrganizationNameAgent(nameAgentRequest);
  }

  private static String getWatchlistPartyNames(HitData hitData) {
    return Optional.ofNullable(hitData.getHitAndWlPartyData().getName())
        .orElse("");
  }

  private static List<String> getAlertedPartyNames(HitData hitData) {
    return Optional.ofNullable(hitData.getAlertedPartyData().getNames())
        .orElseGet(Collections::emptyList);
  }
}
