package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HideOriginDto implements Serializable {

  private static final long serialVersionUID = -7791962038296486349L;
  @JsonProperty("HideOrigin")
  String hideOrigin;
}
