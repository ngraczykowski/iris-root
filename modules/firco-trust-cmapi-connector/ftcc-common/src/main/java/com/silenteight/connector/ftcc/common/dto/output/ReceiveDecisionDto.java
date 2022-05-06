package com.silenteight.connector.ftcc.common.dto.output;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class ReceiveDecisionDto {

  @Builder.Default
  String versionTag = "1";

  FircoAuthenticationDto authentication;

  List<ReceiveDecisionMessageDto> messages;
}
