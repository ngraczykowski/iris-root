package com.silenteight.searpayments.scb.mapper;

import com.silenteight.searpayments.scb.etl.response.HitData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;

@RequiredArgsConstructor
class CreateDelimiterInNameLineAgentRequest {

  @NonNull private final HitData requestHitDto;

  String create() {
    DelimiterInNameLineAgentRequest delimiterInNameLineAgentRequest = DelimiterInNameLineAgentRequest.builder()
        .allMatchingFieldsValue(String.join(
            " ", requestHitDto.getHitAndWlPartyData().getAllMatchingFieldValues()))
        .messageFieldStructureText(requestHitDto
            .getHitAndWlPartyData()
            .getMessageStructure()
            .getMessageFieldStructure()
            .name())
        .build();
    return serializeDelimiterInNameLineAgentRequest(delimiterInNameLineAgentRequest);
  }

  private String serializeDelimiterInNameLineAgentRequest(DelimiterInNameLineAgentRequest request) {
    return INSTANCE.serializeToString(request);
  }
}
