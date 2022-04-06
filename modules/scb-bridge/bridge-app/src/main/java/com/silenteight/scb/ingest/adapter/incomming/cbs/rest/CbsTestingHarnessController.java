package com.silenteight.scb.ingest.adapter.incomming.cbs.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.quartz.QueuingJob;
import com.silenteight.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobConstants;
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationRepository;
import com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert.RawAlertRepository;

import org.quartz.*;
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
@RequiredArgsConstructor
@Slf4j
public class CbsTestingHarnessController {

  private static final String JOB_NAME = "on-demand-cbs-test-job";

  private final RawAlertRepository rawAlertRepository;
  private final ScbRecommendationRepository scbRecommendationRepository;
  private final Scheduler scheduler;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<?> invoke(@Valid @RequestBody CbsInvokeQueueingJobRequest request) throws
      SchedulerException {

    cleanup();
    scheduleJob(request.toAlertIdContext());

    return ResponseEntity.ok().build();
  }

  private void scheduleJob(AlertIdContext alertIdContext) throws SchedulerException {
    JobDetail jobDetail = JobBuilder
        .newJob(QueuingJob.class)
        .setJobData(createQueuingJobDataMap(alertIdContext))
        .build();

    log.info("Scheduling test job for execution by: {}", alertIdContext);
    scheduler.scheduleJob(jobDetail, runOnceTrigger());
  }

  private void cleanup() {
    log.info("Deleting all Raw Alerts");
    rawAlertRepository.deleteAll();

    log.info("Deleting all Recommendations");
    scbRecommendationRepository.deleteAll();
  }

  private static Trigger runOnceTrigger() {
    return TriggerBuilder.newTrigger().build();
  }

  private static JobDataMap createQueuingJobDataMap(AlertIdContext alertIdContext) {
    return new JobDataMap(
        Map.of(QueuingJobConstants.CONTEXT, alertIdContext, QueuingJobConstants.NAME, JOB_NAME));
  }

}
