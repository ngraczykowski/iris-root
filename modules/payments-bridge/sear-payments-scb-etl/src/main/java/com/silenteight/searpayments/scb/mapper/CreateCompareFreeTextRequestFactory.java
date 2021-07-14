package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateCompareFreeTextRequestFactory {

  public CreateCompareFreeTextRequest create(HitData requestHitDto) {
    return new CreateCompareFreeTextRequest(requestHitDto);
  }
}
