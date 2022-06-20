/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

/**
 * The error information and details.
 */
@Validated
@Data
@Builder
public class ErrorResponse {

  @JsonProperty("status")
  private int status;

  @JsonProperty("error")
  @NotNull
  private String error;

  @JsonProperty("timestamp")
  @NotNull
  private LocalDateTime timestamp;

  @JsonProperty("message")
  private String message;
}
