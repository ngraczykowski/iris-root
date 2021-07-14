package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateCompareNamesRequestFactory {

  public CreateCompareNamesRequest create(HitData requestHitDto) {
    return new CreateCompareNamesRequest(requestHitDto);
  }
}
