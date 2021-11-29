package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class OrganizationNameAgentEtlProcess extends BaseAgentEtlProcess<NameFeatureInput> {

  private static final String ORGANIZATION_NAME_AGENT_FEATURE = "organizationName";

  OrganizationNameAgentEtlProcess(
      AgentInputServiceBlockingStub blockingStub, Duration timeout) {
    super(blockingStub, timeout);
  }

  @Override
  protected String getFeatureName() {
    return ORGANIZATION_NAME_AGENT_FEATURE;
  }

  @Override
  protected NameFeatureInput getFeatureInput(HitData hitData) {

    var watchlistType = hitData.getHitAndWlPartyData().getWatchlistType();
    var alertedPartyNames = getAlertedPartyNames(hitData);
    var watchlistPartyNames = getWatchlistPartyNames(hitData);

    if (watchlistType == WatchlistType.COMPANY) {
      return NameFeatureInput.newBuilder()
          .setFeature(getFullFeatureName())
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
          .setFeature(getFullFeatureName())
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
