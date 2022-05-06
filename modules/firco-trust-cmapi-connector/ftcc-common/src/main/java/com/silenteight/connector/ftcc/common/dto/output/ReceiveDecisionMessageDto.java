package com.silenteight.connector.ftcc.common.dto.output;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class ReceiveDecisionMessageDto {

  @JsonProperty("Message")
  AlertDecisionMessageDto decisionMessage;
}
