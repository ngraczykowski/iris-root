package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@RequiredArgsConstructor
class CreateOneLinerAgentRequest {

  @NonNull private final HitData requestHitDto;

  String create() {
    OneLinerAgentRequest oneLinerAgentRequest = new OneLinerAgentRequest(
        requestHitDto.getAlertedPartyData().isNoAcctNumFlag(),
        requestHitDto.getAlertedPartyData().getNumOfLines(),
        requestHitDto.getAlertedPartyData().getMessageLength()
    );

    return serializeOneLinerAgentRequest(oneLinerAgentRequest);
}

  private String serializeOneLinerAgentRequest(OneLinerAgentRequest request) {
    return INSTANCE.serializeToString(request);
  }
}
