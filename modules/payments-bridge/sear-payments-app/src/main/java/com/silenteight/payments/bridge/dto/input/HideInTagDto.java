package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HideInTagDto implements Serializable {

  private static final long serialVersionUID = -538343027372931067L;
  @JsonProperty("HideInTag")
  String hideInTag;
}
