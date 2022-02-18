package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
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

  @NotNull
  public List<String> findCities() {
    return cities.stream().map(AddressCityDto::getCity).collect(Collectors.toList());
  }

  @NotNull
  public List<String> findStates() {
    return states.stream().map(AddressStateDto::getState).collect(Collectors.toList());
  }

  @NotNull
  public List<String> findCountries() {
    return countries.stream().map(AddressCountryDto::getCountry).collect(Collectors.toList());
  }
}
