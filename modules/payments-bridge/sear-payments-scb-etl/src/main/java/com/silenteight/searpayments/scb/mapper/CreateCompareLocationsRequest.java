package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.agent.geo.v1.api.CompareLocationsRequest;
import com.silenteight.searpayments.scb.etl.response.HitData;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.google.protobuf.util.JsonFormat.Printer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
class CreateCompareLocationsRequest {

  @NonNull private final HitData requestHitDto;

  private static final Printer PROTO_JSON_PRINTER =
      JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace();

  String create() {
    CompareLocationsRequest compareLocationsRequest = CompareLocationsRequest.newBuilder()
        .setAlertedPartyLocation(getCountryTownIfExists())
        .setWatchlistPartyLocation(String.join(
            ", ",
            getWatchlistCountryIfExists(),
            getWatchlistStateIfExists(),
            getWatchlistCityIfExists())
        )
        .build();

    return serializeGeoAgentRequest(compareLocationsRequest);
  }

  @NotNull
  private String getCountryTownIfExists() {
    return requestHitDto.getAlertedPartyData().getCtryTowns().stream().findFirst().orElse("");
  }

  @NotNull
  private String getWatchlistCityIfExists() {
    return requestHitDto.getHitAndWlPartyData().getCities().stream().findFirst().orElse("");
  }

  @NotNull
  private String getWatchlistStateIfExists() {
    return requestHitDto.getHitAndWlPartyData().getStates().stream().findFirst().orElse("");
  }

  private String getWatchlistCountryIfExists() {
    List<String> countries = requestHitDto.getHitAndWlPartyData().getCountries();
    return countries.isEmpty() ? "" : countries.get(0);
  }

  private String serializeGeoAgentRequest(CompareLocationsRequest request) {
    try {
      return PROTO_JSON_PRINTER.print(request);
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
