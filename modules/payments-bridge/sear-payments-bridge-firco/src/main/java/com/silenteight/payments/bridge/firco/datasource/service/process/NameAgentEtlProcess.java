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
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import com.google.protobuf.Any;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
class NameAgentEtlProcess implements EtlProcess {

  private final AgentInputServiceBlockingStub blockingStub;
  private final Duration timeout;

  @Override
  public void extractAndLoad(AlertRegisteredEvent data, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    data.getMatches().entrySet().forEach(matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    filterHitsData(hitsData, matchItem).stream()
        .filter(Objects::nonNull)
        .forEach(hitData -> callAgentService(matchItem, hitData));
  }

  @Nonnull
  private List<HitData> filterHitsData(List<HitData> hitsData, Entry<String, String> matchItem) {
    return hitsData.stream()
        .filter(hitData -> matchItem.getKey().equals(
            Optional.of(hitData)
                .map(HitData::getHitAndWlPartyData)
                .map(HitAndWatchlistPartyData::getId)
                .orElse(null)))
        .collect(Collectors.toList());

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

  @Override
  public boolean supports(AlertRegisteredEvent command) {
    return true;
  }

  private BatchCreateAgentInputsRequest createBatchInputRequest(
      String matchValue, List<String> alertedPartNames, String watchlistName,
      EntityType alertedPartyType,
      List<String> matchingTexts) {
    var batchInputRequest = BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(AgentInput.newBuilder()
            .setMatch(matchValue)
            .addFeatureInputs(FeatureInput
                .newBuilder()
                .setFeature("features/name")
                .setAgentFeatureInput(Any.pack(NameFeatureInput.newBuilder()
                    .setFeature("name")
                    .addAllAlertedPartyNames(alertedPartNames
                        .stream()
                        .map(alertedPartyName -> AlertedPartyName
                            .newBuilder()
                            .setName(alertedPartyName)
                            .build())
                        .collect(
                            Collectors.toList()))
                    .addWatchlistNames(WatchlistName.newBuilder()
                        .setName(watchlistName)
                        .build())
                    .setAlertedPartyType(alertedPartyType)
                    .addAllMatchingTexts(matchingTexts)
                    .build()))
                .build())
            .build())
        .build();

    return batchInputRequest;
  }

  @NonNull
  private static EntityType mapWatchListTypeToEntityType(WatchlistType watchlistType) {

    switch (watchlistType) {
      case INDIVIDUAL:
        return EntityType.INDIVIDUAL;
      case COMPANY:
        return EntityType.ORGANIZATION;
      case ADDRESS:
        return EntityType.ENTITY_TYPE_UNSPECIFIED;
      case VESSEL:
        return EntityType.ENTITY_TYPE_UNSPECIFIED;
      default:
        throw new UnsupportedOperationException();
    }
  }
}
