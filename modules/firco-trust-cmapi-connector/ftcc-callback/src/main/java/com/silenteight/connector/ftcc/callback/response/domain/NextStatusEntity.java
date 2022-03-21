package com.silenteight.connector.ftcc.callback.response.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonNaming(UpperCamelCaseStrategy.class)
public class NextStatusEntity {

  private StatusEntity status;

  public StatusInfoDto statusInfoDto() {
    return new StatusInfoDto(status.getId(), status.getName(), status.getRoutingCode(),
        status.getChecksum());
  }
}
