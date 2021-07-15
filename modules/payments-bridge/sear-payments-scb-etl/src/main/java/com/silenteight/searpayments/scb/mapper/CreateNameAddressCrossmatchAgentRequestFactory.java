package com.silenteight.searpayments.scb.mapper;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateNameAddressCrossmatchAgentRequestFactory {

  @NonNull
  private final CreateAlertPartyEntitiesFactory createAlertPartyEntitiesFactory;

  public CreateNameAddressCrossmatchAgentRequest create(HitData requestHitDto) {
    return new CreateNameAddressCrossmatchAgentRequest(
        requestHitDto, createAlertPartyEntitiesFactory);
  }
}
