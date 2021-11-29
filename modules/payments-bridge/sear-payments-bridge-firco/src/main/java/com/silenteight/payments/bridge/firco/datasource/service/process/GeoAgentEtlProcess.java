package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class GeoAgentEtlProcess extends BaseAgentEtlProcess<LocationFeatureInput> {

  private static final String GEO_FEATURE = "geo";

  GeoAgentEtlProcess(
      AgentInputServiceBlockingStub blockingStub, Duration timeout) {
    super(blockingStub, timeout);
  }

  @Override
  protected String getFeatureName() {
    return GEO_FEATURE;
  }

  @Override
  protected LocationFeatureInput getFeatureInput(HitData hitData) {

    var countryTown = getCountryTown(hitData);
    var watchListLocation = getWatchListLocation(hitData);

    return LocationFeatureInput.newBuilder()
        .setFeature(getFullFeatureName())
        .setAlertedPartyLocation(countryTown)
        .setWatchlistLocation(watchListLocation)
        .build();
  }

  private static String getCountryTown(HitData hitData) {
    return getSpecifiedAlertedPartyLocation(
        hitData.getAlertedPartyData(),
        AlertedPartyData::getCtryTowns);
  }

  private static String getSpecifiedAlertedPartyLocation(
      AlertedPartyData alertedPartyData,
      Function<AlertedPartyData, List<String>> getSpecifiedLocation) {
    return Optional.of(alertedPartyData)
        .map(getSpecifiedLocation)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");
  }

  private static String getWatchListLocation(HitData hitData) {
    String country = getSpecifiedWatchListLocation(
        hitData.getHitAndWlPartyData(),
        HitAndWatchlistPartyData::getCountries);
    String state = getSpecifiedWatchListLocation(
        hitData.getHitAndWlPartyData(),
        HitAndWatchlistPartyData::getStates);
    String city = getSpecifiedWatchListLocation(
        hitData.getHitAndWlPartyData(),
        HitAndWatchlistPartyData::getCities);

    return String.join(", ", country, state, city);
  }

  private static String getSpecifiedWatchListLocation(
      HitAndWatchlistPartyData hitAndWatchlistPartyData,
      Function<HitAndWatchlistPartyData, List<String>> getSpecifiedLocation) {
    return Optional.of(hitAndWatchlistPartyData)
        .map(getSpecifiedLocation)
        .orElseGet(Collections::emptyList)
        .stream()
        .findFirst()
        .orElse("");
  }
}
