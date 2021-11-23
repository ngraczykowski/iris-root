package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.AlertedPartyName;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.datasource.api.name.v1.WatchlistName;
import com.silenteight.datasource.api.name.v1.WatchlistName.NameType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@RequiredArgsConstructor
class NameAgentEtlProcess implements EtlProcess {

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

    var batchInputRequest = createBatchInputRequest(
        matchItem.getValue(),
        hitData.getAlertedPartyData().getNames(),
        hitData.getHitAndWlPartyData().getName(),
        mapWatchListTypeToEntityType(hitData.getHitAndWlPartyData().getWatchlistType()),
        hitData.getHitAndWlPartyData().getAllMatchingTexts());

    blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private static BatchCreateAgentInputsRequest createBatchInputRequest(
      String matchValue, List<String> alertedPartyNames, String watchlistName,
      EntityType alertedPartyType,
      List<String> matchingTexts) {

    var nameFeatureInput = NameFeatureInput.newBuilder()
        .setFeature("features/name")
        .addAllAlertedPartyNames(alertedPartyNames
            .stream()
            .map(alertedPartyName -> AlertedPartyName
                .newBuilder()
                .setName(alertedPartyName)
                .build())
            .collect(
                Collectors.toList()))
        .addWatchlistNames(WatchlistName.newBuilder()
            .setName(watchlistName)
            .setType(NameType.REGULAR)
            .build())
        .setAlertedPartyType(alertedPartyType)
        .addAllMatchingTexts(matchingTexts)
        .build();

    return BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(AgentInput.newBuilder()
            .setMatch(matchValue)
            .addFeatureInputs(FeatureInput
                .newBuilder()
                .setFeature("features/name")
                .setAgentFeatureInput(Any.pack(nameFeatureInput))
                .build())
            .build())
        .build();
  }

  @NonNull
  private static EntityType mapWatchListTypeToEntityType(WatchlistType watchlistType) {

    switch (watchlistType) {
      case INDIVIDUAL:
        return EntityType.INDIVIDUAL;
      case COMPANY:
        return EntityType.ORGANIZATION;
      case ADDRESS:
      case VESSEL:
        return EntityType.ENTITY_TYPE_UNSPECIFIED;
      default:
        throw new UnsupportedOperationException();
    }
  }
}
