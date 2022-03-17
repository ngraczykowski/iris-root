package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
@JsonInclude(Include.NON_NULL)
public class GnsRtRecommendationRequest {

  @JsonProperty("screenCustomerNameRes")
  @NotNull
  @Valid
  private GnsRtScreenCustomerNameRes screenCustomerNameRes;
}
