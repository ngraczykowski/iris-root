package com.silenteight.connector.ftcc.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
class PositionDto implements Serializable {

  private static final long serialVersionUID = 6840766953378816340L;

  private String positionStart; // "609"

  private String positionEnd; // "612"
}
