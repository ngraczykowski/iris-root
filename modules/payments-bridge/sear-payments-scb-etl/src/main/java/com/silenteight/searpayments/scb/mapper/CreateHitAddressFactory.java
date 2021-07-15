package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitAndWatchlistPartyData;

@RequiredArgsConstructor
class CreateHitAddressFactory {

  public CreateHitAddress create(HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return new CreateHitAddress(hitAndWatchlistPartyData);
  }
}
