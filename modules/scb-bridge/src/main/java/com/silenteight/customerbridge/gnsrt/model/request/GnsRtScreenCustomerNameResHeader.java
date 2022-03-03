package com.silenteight.customerbridge.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class GnsRtScreenCustomerNameResHeader {

  @JsonProperty("originationDetails")
  @NotNull
  @Valid
  private GnsRtOriginationDetails originationDetails;
}
