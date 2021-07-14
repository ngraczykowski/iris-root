package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateHitsFactory {

  @NonNull private final CreateHitFactory createHitFactory;

  public CreateHits create(AlertEtlResponse alertEtlResponse) {
    return new CreateHits(createHitFactory, alertEtlResponse);
  }
}
