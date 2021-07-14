package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateMatchtextFirstTokenOfAddressAgentRequestFactory {

  public CreateMatchtextFirstTokenOfAddressAgentRequest create(HitData requestHitDto) {
    return new CreateMatchtextFirstTokenOfAddressAgentRequest(requestHitDto);
  }
}
