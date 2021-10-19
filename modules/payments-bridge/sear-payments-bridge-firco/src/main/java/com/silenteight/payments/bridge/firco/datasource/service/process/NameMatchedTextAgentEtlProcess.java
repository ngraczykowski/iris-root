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
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@RequiredArgsConstructor
class NameMatchedTextAgentEtlProcess implements EtlProcess {

  private final AgentInputServiceBlockingStub blockingStub;
  private final Duration timeout;

  @Override
  public void extractAndLoad(AlertRegisteredEvent data, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    data.getMatches().entrySet().forEach(matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    filterHitsData(hitsData, matchItem)
        .forEach(hitData -> callAgentService(matchItem, hitData));
  }

  private void callAgentService(Entry<String, String> matchItem, HitData hitData) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    var batchInputRequest = createBatchInputRequest(
        matchItem.getValue(),
        hitData.getHitAndWlPartyData().getMatchingText(),
        hitData.getHitAndWlPartyData().getName(),
        mapWatchListTypeToEntityType(hitData.getHitAndWlPartyData().getWatchlistType()),
        hitData.getHitAndWlPartyData().getAllMatchingTexts());

    blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private static BatchCreateAgentInputsRequest createBatchInputRequest(
      String matchValue, String matchingText, String watchlistName,
      EntityType alertedPartyType,
      List<String> matchingTexts) {

    var matchingTextAsAlertedPartyName =
        AlertedPartyName.newBuilder().setName(matchingText).build();

    var watchlistNameAsObject = WatchlistName.newBuilder()
        .setName(watchlistName)
        .setType(NameType.REGULAR)
        .build();

    return BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(AgentInput.newBuilder()
            .setMatch(matchValue)
            .addFeatureInputs(FeatureInput
                .newBuilder()
                .setFeature("features/nameMatchedText")
                .setAgentFeatureInput(Any.pack(NameFeatureInput.newBuilder()
                    .setFeature("features/nameMatchedText")
                    .addAlertedPartyNames(matchingTextAsAlertedPartyName)
                    .addWatchlistNames(watchlistNameAsObject)
                    .setAlertedPartyType(alertedPartyType)
                    .addAllMatchingTexts(matchingTexts)
                    .build()))
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

  @Override
  public boolean supports(AlertRegisteredEvent command) {
    return true;
  }
}

//
//  var watchListNameAsAlertedPartyName = List.of(AlertedPartyName
//      .newBuilder()
//      .setName(watchListName)
//      .build());
//
//  var matchingTextAsWatchlist = WatchlistName.newBuilder()
//      .setName(matchingText)
//      .setType(NameType.REGULAR)
//      .build();
//
//    return BatchCreateAgentInputsRequest.newBuilder()
//        .addAgentInputs(AgentInput.newBuilder()
//        .setMatch(matchValue)
//        .addFeatureInputs(FeatureInput
//        .newBuilder()
//        .setFeature("features/nameMatchedText")
//        .setAgentFeatureInput(Any.pack(NameFeatureInput.newBuilder()
//        .setFeature("features/nameMatchedText")
//        .addAllAlertedPartyNames(matchingTextAsWatchlist)
//        .addWatchlistNames(watchListNameAsAlertedPartyName)
//        .setAlertedPartyType(alertedPartyType)
//        .addAllMatchingTexts(matchingTexts)
//        .build()))
//        .build())
//        .build())
//        .build();
