package com.silenteight.scb.ingest.adapter.incomming.cbs.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.OffsetDateTime;

import static java.time.OffsetDateTime.now;

@RequiredArgsConstructor
@Slf4j
class AlertUnderProcessingCleaner implements Job {

  private final AlertInFlightService alertInFlightService;
  private final AlertUnderProcessingCleanerProperties properties;

  @Override
  public void execute(JobExecutionContext context) {
    OffsetDateTime expireDate = now().minus(properties.getOffset());
    alertInFlightService.deleteExpired(expireDate);

    log.info("Alert under processing cleaner job has been finished.");
  }
}
