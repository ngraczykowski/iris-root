package com.silenteight.payments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressStateDto implements Serializable {

  private static final long serialVersionUID = 5412840580658824406L;
  @JsonProperty("State")
  String state;
}
