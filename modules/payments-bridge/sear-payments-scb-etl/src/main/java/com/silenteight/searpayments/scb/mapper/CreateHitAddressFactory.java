package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitAndWatchlistPartyData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateHitAddressFactory {

  public CreateHitAddress create(HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return new CreateHitAddress(hitAndWatchlistPartyData);
  }
}
