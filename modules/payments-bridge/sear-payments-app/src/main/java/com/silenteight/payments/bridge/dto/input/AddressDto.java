package com.silenteight.payments.bridge.dto.input;

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
  String isMain; // "1"
  @JsonProperty("PostalAddress")
  String postalAddress; // ""
  @JsonProperty("Cities")
  List<AddressCityDto> cities;
  @JsonProperty("States")
  List<AddressStateDto> states;
  @JsonProperty("Countries")
  List<AddressCountryDto> countries;
}
