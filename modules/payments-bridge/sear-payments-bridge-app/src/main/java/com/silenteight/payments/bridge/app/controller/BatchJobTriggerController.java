package com.silenteight.payments.bridge.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.app.batch.JobMaintainer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class BatchJobTriggerController {

  private final JobMaintainer jobMaintainer;

  @PostMapping("/batch/trigger")
  public ResponseEntity<String> triggerBatchJob(@RequestBody TriggerBatchJobRequest request) {
    log.info("Received trigger batch job request = {}", request);

    try {
      jobMaintainer.runJobByName(request.getJobName(), request.toJobParameters());
      return ResponseEntity.ok(
          String.format("Successfully triggered %s batch job", request.getJobName()));
    } catch (Exception e) {
      log.error("Failed to run job {}: {}", request.fileName, e);
      return ResponseEntity
          .badRequest()
          .body(String.format("Failed to run %s batch job", request.getJobName()));
    }
  }
}
