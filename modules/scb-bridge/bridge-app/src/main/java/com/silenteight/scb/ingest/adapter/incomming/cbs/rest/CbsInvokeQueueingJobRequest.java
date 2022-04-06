package com.silenteight.scb.ingest.adapter.incomming.cbs.rest;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdContext;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class CbsInvokeQueueingJobRequest {

  private boolean ackRecords = true;

  @NotNull
  private String hitDetailsView = "";

  @Min(1)
  @Max(10)
  private int priority = 10;

  private boolean watchlistLevel = true;

  @NotEmpty
  private String recordsView = "SENS_V_FFF_RECORDS_DENY";

  @Min(1)
  private int chunkSize = 1000;

  @Min(1)
  private int totalRecordsToRead = 1;

  public AlertIdContext toAlertIdContext() {
    return AlertIdContext.builder()
        .recordsView(recordsView)
        .ackRecords(ackRecords)
        .priority(priority)
        .watchlistLevel(watchlistLevel)
        .chunkSize(chunkSize)
        .hitDetailsView(hitDetailsView)
        .totalRecordsToRead(totalRecordsToRead)
        .build();
  }

}

