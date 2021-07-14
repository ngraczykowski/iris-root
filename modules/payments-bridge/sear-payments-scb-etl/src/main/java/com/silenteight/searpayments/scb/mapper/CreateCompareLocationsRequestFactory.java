package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateCompareLocationsRequestFactory {

  public CreateCompareLocationsRequest create(HitData requestHitDto) {
    return new CreateCompareLocationsRequest(requestHitDto);
  }
}
