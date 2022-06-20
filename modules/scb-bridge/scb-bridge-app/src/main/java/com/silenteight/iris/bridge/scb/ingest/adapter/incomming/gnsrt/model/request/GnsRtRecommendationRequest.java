/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

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
