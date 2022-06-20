/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@Validated
@Data
public class GnsRtSilent8Response {

  @JsonProperty("alerts")
  @Valid
  private List<GnsRtResponseAlert> alerts = new ArrayList<>();
}
