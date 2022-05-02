package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;

@Value
@Builder
@Jacksonized
class AckMessageDto {

  @JsonProperty("faultcode")
  String faultCode;

  @JsonProperty("faultstring")
  String faultString;

  @JsonProperty("faultactor")
  @Builder.Default
  String faultActor = "cmapi.send.message";
}
