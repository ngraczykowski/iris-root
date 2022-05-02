package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@Builder
@Jacksonized
class AckBodyDto {

  @JsonProperty("msg_Acknowledgement")
  AckMessageDto messageDto;
}
