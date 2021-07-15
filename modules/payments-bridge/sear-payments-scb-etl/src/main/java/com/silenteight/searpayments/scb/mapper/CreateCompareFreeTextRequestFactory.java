package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateCompareFreeTextRequestFactory {

  public CreateCompareFreeTextRequest create(HitData requestHitDto) {
    return new CreateCompareFreeTextRequest(requestHitDto);
  }
}
