package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCityDto implements Serializable {

  private static final long serialVersionUID = -8614771981069000883L;

  @JsonProperty("City")
  private String city;
}
