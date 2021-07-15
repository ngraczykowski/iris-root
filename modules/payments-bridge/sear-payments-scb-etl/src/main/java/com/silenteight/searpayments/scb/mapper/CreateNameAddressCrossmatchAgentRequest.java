package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

import java.util.List;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@RequiredArgsConstructor
class CreateNameAddressCrossmatchAgentRequest {

  @NonNull private final HitData requestHitDto;
  @NonNull private final CreateAlertPartyEntitiesFactory createAlertPartyEntitiesFactory;

  String create() {
    NameAddressCrossmatchAgentRequest nameAddressCrossmatchAgentRequest =
        NameAddressCrossmatchAgentRequest.builder()
            .alertPartyEntities(createAlertPartyEntitiesFactory.create(requestHitDto).create())
            .watchlistName(requestHitDto.getHitAndWlPartyData().getName())
            .watchlistType(requestHitDto.getHitAndWlPartyData().getWatchlistType().getName())
            .watchlistCountry(getWatchlistCountryIfExists())
            .build();

    return serializeNameAddressCrossmatchAgentRequest(nameAddressCrossmatchAgentRequest);
  }

  private String getWatchlistCountryIfExists() {
    List<String> countries = requestHitDto.getHitAndWlPartyData().getCountries();
    return countries.isEmpty() ? "" : countries.get(0);
  }

  private String serializeNameAddressCrossmatchAgentRequest(
      NameAddressCrossmatchAgentRequest request) {
    return INSTANCE.serializeToString(request);
  }
}
