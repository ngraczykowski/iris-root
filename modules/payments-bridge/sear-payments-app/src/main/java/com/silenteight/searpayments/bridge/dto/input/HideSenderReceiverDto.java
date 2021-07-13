package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HideSenderReceiverDto implements Serializable {

  private static final long serialVersionUID = 2946965551477913215L;
  @JsonProperty("HideSenderReceiver")
  String hideSenderReceiver;
}
