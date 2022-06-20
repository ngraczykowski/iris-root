/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.request;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class GnsRtHit {

  @JsonProperty("hitID")
  private String hitId;

  @JsonProperty("hitDetails")
  private String hitDetails;
}
