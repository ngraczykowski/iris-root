/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertIdReaderContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.TestingHarnessService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationRepository;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertRepository;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.quartz.QueuingJob;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobConstants;

import org.quartz.*;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/cbs/test")
@AllArgsConstructor
@Slf4j
@Conditional(OnAlertProcessorCondition.class)
public class CbsTestingHarnessController {

  private static final String JOB_NAME = "on-demand-cbs-test-job";

  private final RawAlertRepository rawAlertRepository;
  private final ScbRecommendationRepository scbRecommendationRepository;
  private final Scheduler scheduler;
  private final TestingHarnessService testHarnessService;

  @PostMapping(path = "/cleanup")
  public ResponseEntity<?> cleanup() {

    log.info("Deleting all Raw Alerts");
    rawAlertRepository.deleteAll();

    log.info("Deleting all Recommendations");
    scbRecommendationRepository.deleteAll();

    return ResponseEntity.ok().build();
  }

  @PostMapping(path = "/invokeQueueingJob", consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> invokeQueueingJob(
      @Valid @RequestBody CbsInvokeQueueingJobRequest request) throws
      SchedulerException {

    log.info("Request: {}", request);
    scheduleJob(AlertIdReaderContext.builder()
        .alertIdContext(request.getAlertIdContext().toAlertIdContext())
        .chunkSize(request.getChunkSize())
        .totalRecordsToRead(request.getTotalRecordsToRead())
        .build());

    return ResponseEntity.ok().build();
  }

  @PostMapping(path = "/queueAlert", consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> queueAlert(@Valid @RequestBody CbsQueueAlertRequest request) {

    log.info("Request: {}", request);
    testHarnessService.queueAlert(
        request.getAlertIdContext().toAlertIdContext(),
        request.getSystemId());

    return ResponseEntity.ok().build();
  }

  private void scheduleJob(AlertIdReaderContext alertIdReaderContext) throws SchedulerException {
    JobDetail jobDetail = JobBuilder
        .newJob(QueuingJob.class)
        .setJobData(createQueuingJobDataMap(alertIdReaderContext))
        .build();

    log.info("Scheduling test job for execution by: {}", alertIdReaderContext);
    scheduler.scheduleJob(jobDetail, runOnceTrigger());
  }

  private static Trigger runOnceTrigger() {
    return TriggerBuilder.newTrigger().build();
  }

  private static JobDataMap createQueuingJobDataMap(AlertIdReaderContext alertIdReaderContext) {
    return new JobDataMap(
        Map.of(
            QueuingJobConstants.CONTEXT, alertIdReaderContext, QueuingJobConstants.NAME,
            JOB_NAME));
  }

}
