package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateOneLinerAgentRequestFactory {

  public CreateOneLinerAgentRequest create(HitData requestHitDto) {
    return new CreateOneLinerAgentRequest(requestHitDto);
  }
}
