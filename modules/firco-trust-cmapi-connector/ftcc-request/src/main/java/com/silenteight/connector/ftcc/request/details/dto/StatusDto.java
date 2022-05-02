package com.silenteight.connector.ftcc.request.details.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class StatusDto {

  @JsonProperty("ID")
  String id;
  String name;
  String routingCode;
  String checksum;
}
