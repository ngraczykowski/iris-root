package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateCompareLocationsRequestFactory {

  public CreateCompareLocationsRequest create(HitData requestHitDto) {
    return new CreateCompareLocationsRequest(requestHitDto);
  }
}
