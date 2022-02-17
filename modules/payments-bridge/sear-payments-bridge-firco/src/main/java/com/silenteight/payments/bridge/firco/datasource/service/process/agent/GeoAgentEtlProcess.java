package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.agents.model.GeoAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateLocationFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.GEO_FEATURE;

@Component
@RequiredArgsConstructor
class GeoAgentEtlProcess extends BaseAgentEtlProcess<LocationFeatureInput> {

  private final CreateLocationFeatureInputUseCase createLocationFeatureInputUseCase;

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {

    var countryTown = getCountryTown(hitData);
    var watchListLocation = getWatchListLocation(hitData);

    var geoAgentRequest = GeoAgentRequest.builder()
        .feature(GEO_FEATURE)
        .alertedPartyLocation(countryTown)
        .watchlistLocation(watchListLocation)
        .build();

    var locationFeatureInput = createLocationFeatureInputUseCase.create(geoAgentRequest);

    var featureInput = FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(GEO_FEATURE))
        .setAgentFeatureInput(Any.pack(locationFeatureInput))
        .build();

    return List.of(featureInput);
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
