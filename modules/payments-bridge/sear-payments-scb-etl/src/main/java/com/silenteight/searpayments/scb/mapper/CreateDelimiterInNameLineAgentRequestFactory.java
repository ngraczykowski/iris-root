package com.silenteight.searpayments.scb.mapper;


import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreateDelimiterInNameLineAgentRequestFactory {

  public CreateDelimiterInNameLineAgentRequest create(HitData requestHitDto) {
    return new CreateDelimiterInNameLineAgentRequest(requestHitDto);
  }
}
