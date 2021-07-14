package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateAlertPartyEntitiesFactory {

  CreateAlertPartyEntities create(HitData requestHitDto) {
    return new CreateAlertPartyEntities(requestHitDto);
  }
}
