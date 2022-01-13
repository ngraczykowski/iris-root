package com.silenteight.payments.bridge.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.app.learning.JobMaintainer;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.HISTORICAL_RISK_ASSESSMENT_JOB_NAME;

@RestController
@RequiredArgsConstructor
@Slf4j
class LearningTriggerController {

  private final JobMaintainer jobMaintainer;

  @PostMapping("/hdl/trigger")
  public ResponseEntity triggerHistoricalDecisionLearning() {
    log.info("Trigger Historical Decision Learning Job");
    var job = jobMaintainer.runJobByName(
        HISTORICAL_RISK_ASSESSMENT_JOB_NAME, new JobParametersBuilder().addDate(
            "api-triggered",
            Calendar.getInstance().getTime()).toJobParameters());
    if (job.isPresent()) {
      return ResponseEntity.ok(
          String.format("Historical Decision Learning Job triggered: %d", job.get().getJobId()));
    } else {
      log.error("Could not execute historical decision Job");
      return ResponseEntity.notFound().build();
    }
  }
}
