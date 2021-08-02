package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddressDto implements Serializable {

  private static final long serialVersionUID = -8490002443962591177L;

  @JsonProperty("Address")
  private AddressDto address;
}
