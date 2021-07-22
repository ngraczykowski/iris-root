package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class RequestActionDto implements Serializable {

  private static final long serialVersionUID = -8592264042137920661L;
  @JsonProperty("Action")
  ActionDto action;
}
