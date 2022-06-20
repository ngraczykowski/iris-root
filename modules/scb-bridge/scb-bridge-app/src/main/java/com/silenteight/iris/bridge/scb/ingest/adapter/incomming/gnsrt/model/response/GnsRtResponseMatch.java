/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@Builder
public class GnsRtResponseMatch {

  @JsonProperty("hitID")
  @NotNull
  private String hitID;

  @JsonProperty("stepId")
  @NotNull
  @JsonInclude(Include.NON_EMPTY)
  private String stepId;

  @JsonProperty("fvSignature")
  @NotNull
  private String fvSignature;

  @JsonProperty("qaSampled")
  @JsonInclude(Include.NON_EMPTY)
  String qaSampled;
}
