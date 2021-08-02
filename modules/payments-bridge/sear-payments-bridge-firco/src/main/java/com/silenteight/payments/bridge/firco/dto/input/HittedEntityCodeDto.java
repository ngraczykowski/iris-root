package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class HittedEntityCodeDto implements Serializable {

  private static final long serialVersionUID = 164865933921417120L;

  @JsonProperty("Code")
  private CodeDto code;
}
