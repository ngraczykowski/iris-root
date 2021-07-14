package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@RequiredArgsConstructor
class CreateMatchtextFirstTokenOfAddressAgentRequest {

  @NonNull private final HitData requestHitDto;

  String create() {
    MatchtextFirstTokenOfAddressAgentRequest matchtextFirstTokenOfAddressAgentRequest = MatchtextFirstTokenOfAddressAgentRequest.builder()
        .matchingTexts(requestHitDto.getHitAndWlPartyData().getAllMatchingTexts())
        .addresses(requestHitDto.getAlertedPartyData().getAddresses())
        .build();
    return serializeMatchtextFirstTokenOfAddressAgentRequest(matchtextFirstTokenOfAddressAgentRequest);
  }

  private String serializeMatchtextFirstTokenOfAddressAgentRequest(MatchtextFirstTokenOfAddressAgentRequest request) {
    return INSTANCE.serializeToString(request);
  }
}
