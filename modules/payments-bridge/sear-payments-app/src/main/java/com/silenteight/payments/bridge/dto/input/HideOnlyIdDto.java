package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HideOnlyIdDto implements Serializable {

  private static final long serialVersionUID = -4349895414601451957L;
  @JsonProperty("HideOnlyID")
  String hideOnlyID;
}
