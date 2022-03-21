package com.silenteight.connector.ftcc.callback.response.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@AllArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class StatusEntity {

  @JsonProperty("ID")
  private String id;
  private String name;
  private String routingCode;
  private String checksum;
}
