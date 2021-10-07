package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.event.AlertRegisteredEvent;
import com.silenteight.payments.bridge.firco.datasource.model.EtlProcess;
import com.silenteight.payments.bridge.svb.etl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.etl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.etl.response.HitData;

import com.google.protobuf.Any;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.silenteight.payments.bridge.firco.datasource.util.HitDataUtils.filterHitsData;

@RequiredArgsConstructor
class GeoAgentEtlProcess implements EtlProcess {

  private final AgentInputServiceBlockingStub blockingStub;
  private final Duration timeout;

  @Override
  public void extractAndLoad(AlertRegisteredEvent data, AlertEtlResponse alertEtlResponse) {
    List<HitData> hitsData = alertEtlResponse.getHits();
    data.getMatches().entrySet().forEach(matchItem -> handleMatches(hitsData, matchItem));
  }

  private void handleMatches(List<HitData> hitsData, Entry<String, String> matchItem) {
    filterHitsData(hitsData, matchItem).stream()
        .forEach(hitData -> callAgentService(matchItem, hitData));
  }

  private void callAgentService(Entry<String, String> matchItem, HitData hitData) {
    var deadline = Deadline.after(timeout.toMillis(), TimeUnit.MILLISECONDS);

    String countryTown = getSpecifiedAlertedPartyLocation(
        hitData.getAlertedPartyData(),
        AlertedPartyData::getCtryTowns);
    String country = getSpecifiedWatchListLocation(
        hitData.getHitAndWlPartyData(),
        HitAndWatchlistPartyData::getCountries);
    String state = getSpecifiedWatchListLocation(
        hitData.getHitAndWlPartyData(),
        HitAndWatchlistPartyData::getStates);
    String city = getSpecifiedWatchListLocation(
        hitData.getHitAndWlPartyData(),
        HitAndWatchlistPartyData::getCities);

    String watchListLocation = String.join(", ", country, state, city);

    var batchInputRequest = createBatchInputRequest(
        matchItem.getValue(),
        countryTown,
        watchListLocation
    );

    blockingStub
        .withDeadline(deadline)
        .batchCreateAgentInputs(batchInputRequest);
  }

  private BatchCreateAgentInputsRequest createBatchInputRequest(
      String matchValue, String countryTown, String watchListLocation) {

    var batchInputRequest = BatchCreateAgentInputsRequest.newBuilder()
        .addAgentInputs(AgentInput.newBuilder()
            .setMatch(matchValue)
            .addFeatureInputs(
                FeatureInput.newBuilder()
                    .setFeature("features/location")
                    .setAgentFeatureInput(Any.pack(
                        LocationFeatureInput.newBuilder()
                            .setAlertedPartyLocation(countryTown)
                            .setWatchlistLocation(watchListLocation)
                            .build()))
                    .build())
            .build())
        .build();

    return batchInputRequest;
  }

  private String getSpecifiedWatchListLocation(
      HitAndWatchlistPartyData hitAndWatchlistPartyData,
      Function<HitAndWatchlistPartyData, List<String>> getSpecifiedLocation) {
    return Optional.of(hitAndWatchlistPartyData)
        .map(getSpecifiedLocation)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");
  }

  private String getSpecifiedAlertedPartyLocation(
      AlertedPartyData alertedPartyData,
      Function<AlertedPartyData, List<String>> getSpecifiedLocation) {
    return Optional.of(alertedPartyData)
        .map(getSpecifiedLocation)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");
  }

  @Override
  public boolean supports(AlertRegisteredEvent data) {
    return true;
  }
}
