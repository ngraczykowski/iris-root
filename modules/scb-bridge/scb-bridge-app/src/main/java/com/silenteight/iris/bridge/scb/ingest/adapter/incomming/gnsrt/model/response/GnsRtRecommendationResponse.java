/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * The recommendations generated for each of the input alerts.
 */
@Validated
@Data
public class GnsRtRecommendationResponse {

  @JsonProperty("silent8Response")
  private GnsRtSilent8Response silent8Response = new GnsRtSilent8Response();
}
