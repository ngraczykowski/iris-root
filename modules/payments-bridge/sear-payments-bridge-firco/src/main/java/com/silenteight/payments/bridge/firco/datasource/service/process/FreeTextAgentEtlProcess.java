package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput;
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
class FreeTextAgentEtlProcess implements EtlProcess {

  private final AgentInputServiceBlockingStub blockingStub;
  private final Duration timeout;

  @Override
  public void extractAndLoad(AlertRegisteredEvent data, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    data.getMatches().entrySet().forEach(matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    filterHitsData(hitsData, matchItem).parallelStream()
        .filter(Objects::nonNull)
        .forEach(hitData -> callAgentService(matchItem, hitData));
  }

  @Nonnull
  private List<HitData> filterHitsData(List<HitData> hitsData, Entry<String, String> matchItem) {
    return hitsData.parallelStream()
        .filter(hitData -> matchItem.getKey().equals(
            Optional.of(hitData)
                .map(HitData::getHitAndWlPartyData)
                .map(HitAndWatchlistPartyData::getId)
                .orElse(null)))
        .collect(Collectors.toList());

  }

  private void callAgentService(Entry<String, String> matchItem, HitData hitData) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);
    blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(BatchCreateAgentInputsRequest.newBuilder()
            .addAgentInputs(AgentInput.newBuilder()
                .setMatch(matchItem.getValue())
                .addFeatureInputs(getFeatureInput(hitData.getHitAndWlPartyData()))
                .build())
            .build());

  }

  @Nonnull
  private FeatureInput getFeatureInput(HitAndWatchlistPartyData hitAndWlData) {
    return FeatureInput
        .newBuilder()
        .setFeature("features/freeText")
        .setAgentFeatureInput(Any.pack(FreeTextFeatureInput
            .newBuilder()
            .setFeature("freeText")
            .setMatchedName(hitAndWlData.getName())
            .setMatchedType(hitAndWlData.getWatchlistType().getName())
            .setFreetext(extractMatchingTexts(hitAndWlData))
            .addAllMatchingTexts(hitAndWlData.getAllMatchingTexts())
            .build()))
        .build();
  }

  @Nonnull
  private String extractMatchingTexts(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return Optional.of(hitAndWatchlistPartyData)
        .map(HitAndWatchlistPartyData::getFieldValue)
        .orElse("");
  }

  @Override
  public boolean supports(AlertRegisteredEvent command) {
    return true;
  }
}
