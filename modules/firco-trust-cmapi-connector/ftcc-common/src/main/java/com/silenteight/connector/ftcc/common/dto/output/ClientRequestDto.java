package com.silenteight.connector.ftcc.common.dto.output;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
@NonFinal
public class ClientRequestDto {

  @JsonInclude
  String header;

  Body body;

  @Value
  public static class Body {
    @JsonProperty("msg_ReceiveStatusUpdate")
    ReceiveDecisionDto receiveDecisionDto;
  }
}
