/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class GnsRtScreenCustomerNameResInfo {

  @JsonProperty("header")
  @NotNull
  @Valid
  private GnsRtScreenCustomerNameResInfoHeader header;

  @JsonProperty("screenableData")
  @NotNull
  @Valid
  private ScreenableData screenableData;

  @JsonProperty("immediateResponseData")
  @NotNull
  @Valid
  private ImmediateResponseData immediateResponseData;
}
