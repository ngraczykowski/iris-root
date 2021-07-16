package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateMatchtextFirstTokenOfAddressAgentRequestFactory {

  public CreateMatchtextFirstTokenOfAddressAgentRequest create(HitData requestHitDto) {
    return new CreateMatchtextFirstTokenOfAddressAgentRequest(requestHitDto);
  }
}
