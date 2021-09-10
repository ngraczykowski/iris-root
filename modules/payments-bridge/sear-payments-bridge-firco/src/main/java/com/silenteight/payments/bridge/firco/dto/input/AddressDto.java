package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class AddressDto implements Serializable {

  private static final long serialVersionUID = 3581373434271747399L;

  private String isMain; // "1"

  private String postalAddress; // ""

  private List<AddressCityDto> cities;

  private List<AddressStateDto> states;

  private List<AddressCountryDto> countries;
}
