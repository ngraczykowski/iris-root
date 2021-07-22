package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HideUnitDto implements Serializable {

  private static final long serialVersionUID = 5788166045736982767L;
  @JsonProperty("HideUnit")
  String hideUnit;
}
