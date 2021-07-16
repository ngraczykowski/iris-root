package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateOneLinerAgentRequestFactory {

  public CreateOneLinerAgentRequest create(HitData requestHitDto) {
    return new CreateOneLinerAgentRequest(requestHitDto);
  }
}
