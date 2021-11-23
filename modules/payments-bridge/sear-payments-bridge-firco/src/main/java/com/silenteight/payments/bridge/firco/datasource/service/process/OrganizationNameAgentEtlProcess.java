package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@RequiredArgsConstructor
class OrganizationNameAgentEtlProcess implements EtlProcess {

  private static final String ORGANIZATION_NAME_FEATURE = "features/organizationName";

  private final AgentInputServiceBlockingStub blockingStub;
  private final Duration timeout;

  @Override
  public void extractAndLoad(AeAlert alert, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    alert.getMatches().entrySet()
        .forEach(matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    filterHitsData(hitsData, matchItem)
        .forEach(hitData -> callAgentService(matchItem, hitData));
  }

  private void callAgentService(Entry<String, String> matchItem, HitData hitData) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    WatchlistType watchlistType = hitData.getHitAndWlPartyData().getWatchlistType();
    List<String> alertedPartyNames =
        Optional.ofNullable(hitData.getAlertedPartyData().getNames()).orElseGet(
            Collections::emptyList);
    String watchlistPartyNames =
        Optional.ofNullable(hitData.getHitAndWlPartyData().getName()).orElse("");

    var batchInputRequest = createBatchInputRequest(
        matchItem.getValue(),
        watchlistType,
        alertedPartyNames,
        watchlistPartyNames
    );

    blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private BatchCreateAgentInputsRequest createBatchInputRequest(
      String matchValue, WatchlistType watchlistType, List<String> alertedPartyNames,
      String watchlistPartyNames) {

    var createCompareOrganizationNamesRequest =
        createNameFeatureInput(watchlistType, alertedPartyNames,
            watchlistPartyNames);


    return BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(AgentInput.newBuilder()
            .setMatch(matchValue)
            .addFeatureInputs(
                FeatureInput.newBuilder()
                    .setFeature(ORGANIZATION_NAME_FEATURE)
                    .setAgentFeatureInput(Any.pack(createCompareOrganizationNamesRequest))
                    .build())
            .build())
        .build();
  }

  private NameFeatureInput createNameFeatureInput(
      WatchlistType watchlistType, List<String> alertedPartyNames, String watchlistPartyNames) {
    if (watchlistType == WatchlistType.COMPANY) {
      return NameFeatureInput.newBuilder()
          .setFeature(ORGANIZATION_NAME_FEATURE)
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
          .setFeature(ORGANIZATION_NAME_FEATURE)
          .addAllAlertedPartyNames(new ArrayList<>())
          .addWatchlistNames(WatchlistName.newBuilder()
              .setName(watchlistPartyNames)
              .build())
          .build();
    }
  }
}
