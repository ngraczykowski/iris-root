/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.rest;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class CbsInvokeQueueingJobRequest {

  @NotNull
  private AlertIdContextDto alertIdContext = new AlertIdContextDto();

  @Min(1)
  private int chunkSize = 1000;

  @Min(1)
  private int totalRecordsToRead = 1;
}
