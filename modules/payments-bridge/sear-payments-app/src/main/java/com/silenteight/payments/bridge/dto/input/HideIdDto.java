package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HideIdDto implements Serializable {

  private static final long serialVersionUID = 463764000660067724L;
  @JsonProperty("HideID")
  String hideID;
}
