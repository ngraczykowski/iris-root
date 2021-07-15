package com.silenteight.searpayments.scb.mapper;

import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateAlertPartyEntitiesFactory {

  CreateAlertPartyEntities create(HitData requestHitDto) {
    return new CreateAlertPartyEntities(requestHitDto);
  }
}
