package com.silenteight.searpayments.scb.mapper;


import lombok.RequiredArgsConstructor;

import com.silenteight.searpayments.scb.etl.response.HitData;

@RequiredArgsConstructor
class CreateDelimiterInNameLineAgentRequestFactory {

  public CreateDelimiterInNameLineAgentRequest create(HitData requestHitDto) {
    return new CreateDelimiterInNameLineAgentRequest(requestHitDto);
  }
}
