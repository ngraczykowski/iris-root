package com.silenteight.payments.bridge.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestAddressDto implements Serializable {

  private static final long serialVersionUID = -8490002443962591177L;

  private AddressDto address;

  @NotNull
  public List<String> findCities() {
    return address.findCities();
  }

  @NotNull
  public List<String> findStates() {
    return address.findStates();
  }

  @NotNull
  public List<String> findCountries() {
    return address.findCountries();
  }

  @NotNull
  public String findIsMainAddress() {
    return address.getIsMain();
  }

  @NotNull
  public String findPostalAddress() {
    return address.getPostalAddress();
  }
}
