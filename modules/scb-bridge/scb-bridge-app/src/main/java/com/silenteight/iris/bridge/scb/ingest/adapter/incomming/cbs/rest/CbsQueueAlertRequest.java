/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.rest;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
public class CbsQueueAlertRequest {

  @NotNull
  private AlertIdContextDto alertIdContext = new AlertIdContextDto();

  @NotNull
  private String systemId;

}
