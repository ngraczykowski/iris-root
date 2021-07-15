package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateCompareNamesRequestFactory {

  public CreateCompareNamesRequest create(HitData requestHitDto) {
    return new CreateCompareNamesRequest(requestHitDto);
  }
}
