package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {

  private static final long serialVersionUID = 3581373434271747399L;

  @JsonProperty("IsMain")
  private String isMain; // "1"

  @JsonProperty("PostalAddress")
  private String postalAddress; // ""

  @JsonProperty("Cities")
  private List<AddressCityDto> cities;

  @JsonProperty("States")
  private List<AddressStateDto> states;

  @JsonProperty("Countries")
  private List<AddressCountryDto> countries;
}
