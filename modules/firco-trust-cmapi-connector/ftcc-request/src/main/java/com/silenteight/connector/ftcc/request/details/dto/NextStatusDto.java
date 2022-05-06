package com.silenteight.connector.ftcc.request.details.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class NextStatusDto {

  @NonNull
  StatusDto status;

  public StatusInfoDto statusInfoDto() {
    return StatusInfoDto.builder()
        .id(status.getId())
        .name(status.getName())
        .routingCode(status.getRoutingCode())
        .checksum(status.getChecksum())
        .build();
  }
}
