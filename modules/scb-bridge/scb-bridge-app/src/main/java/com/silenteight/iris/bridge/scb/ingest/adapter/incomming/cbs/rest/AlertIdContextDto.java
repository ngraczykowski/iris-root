/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.rest;

import lombok.Data;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertIdContext;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class AlertIdContextDto {

  private boolean ackRecords = true;

  @NotNull
  private String hitDetailsView = "";

  @Min(1)
  @Max(10)
  private int priority = 10;

  private boolean watchlistLevel = true;

  @NotEmpty
  private String recordsView = "SENS_V_FFF_RECORDS_DENY";

  public AlertIdContext toAlertIdContext() {
    return AlertIdContext.builder()
        .recordsView(recordsView)
        .ackRecords(ackRecords)
        .priority(priority)
        .watchlistLevel(watchlistLevel)
        .hitDetailsView(hitDetailsView)
        .build();
  }

}

