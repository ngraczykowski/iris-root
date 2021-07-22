package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RequestPositionDto implements Serializable {

  private static final long serialVersionUID = -3029281541697580372L;
  @JsonProperty("Position")
  PositionDto position;
}
