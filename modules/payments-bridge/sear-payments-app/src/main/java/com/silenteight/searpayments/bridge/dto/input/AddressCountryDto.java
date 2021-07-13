package com.silenteight.searpayments.bridge.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressCountryDto implements Serializable {

  private static final long serialVersionUID = -491961887753150141L;
  @JsonProperty("Country")
  String country;
}
