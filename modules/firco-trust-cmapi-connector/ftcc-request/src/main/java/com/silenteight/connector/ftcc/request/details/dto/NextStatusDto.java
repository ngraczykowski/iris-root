package com.silenteight.connector.ftcc.request.details.dto;

import lombok.*;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonNaming(UpperCamelCaseStrategy.class)
public class NextStatusDto {

  @NonNull
  private StatusDto status;

  public StatusInfoDto statusInfoDto() {
    return new StatusInfoDto(
        status.getId(),
        status.getName(),
        status.getRoutingCode(),
        status.getChecksum());
  }
}
