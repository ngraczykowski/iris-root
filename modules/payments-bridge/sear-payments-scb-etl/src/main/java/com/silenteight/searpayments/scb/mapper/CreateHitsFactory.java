package com.silenteight.searpayments.scb.mapper;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;

@RequiredArgsConstructor
class CreateHitsFactory {

  @NonNull private final CreateHitFactory createHitFactory;

  public CreateHits create(AlertEtlResponse alertEtlResponse) {
    return new CreateHits(createHitFactory, alertEtlResponse);
  }
}
