package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class PositionDto implements Serializable {

  private static final long serialVersionUID = 6840766953378816340L;
  @JsonProperty("PositionStart")
  String positionStart; // "609"
  @JsonProperty("PositionEnd")
  String positionEnd; // "612"
}
