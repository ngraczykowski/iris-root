package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.datasource.port.CreateAgentInputsClient;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.ORGANIZATION_NAME_FEATURE;

class OrganizationNameAgentEtlProcess extends BaseAgentEtlProcess<NameFeatureInput> {

  OrganizationNameAgentEtlProcess(CreateAgentInputsClient createAgentInputsClient) {
    super(createAgentInputsClient);
  }

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    var featureInput = FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(ORGANIZATION_NAME_FEATURE))
        .setAgentFeatureInput(Any.pack(createNameFeatureInput(hitData)))
        .build();

    return List.of(featureInput);
  }

  private static NameFeatureInput createNameFeatureInput(HitData hitData) {

    var watchlistType = hitData.getHitAndWlPartyData().getWatchlistType();
    var alertedPartyNames = getAlertedPartyNames(hitData);
    var watchlistPartyNames = getWatchlistPartyNames(hitData);

    if (watchlistType == WatchlistType.COMPANY) {
      return NameFeatureInput.newBuilder()
          .setFeature(getFullFeatureName(ORGANIZATION_NAME_FEATURE))
          .addAllAlertedPartyNames(alertedPartyNames
              .stream()
              .map(alertedPartyName -> AlertedPartyName
                  .newBuilder()
                  .setName(alertedPartyName)
                  .build())
              .collect(Collectors.toList()))
          .addWatchlistNames(WatchlistName.newBuilder()
              .setName(watchlistPartyNames)
              .build())
          .build();
    } else {
      return NameFeatureInput.newBuilder()
          .setFeature(getFullFeatureName(ORGANIZATION_NAME_FEATURE))
          .addAllAlertedPartyNames(new ArrayList<>())
          .addWatchlistNames(WatchlistName.newBuilder()
              .setName(watchlistPartyNames)
              .build())
          .build();
    }
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
