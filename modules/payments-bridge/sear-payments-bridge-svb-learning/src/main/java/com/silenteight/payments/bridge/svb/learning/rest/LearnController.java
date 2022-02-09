package com.silenteight.payments.bridge.svb.learning.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class LearnController {

  private final HandleLearningAlertsUseCase handleLearningAlertsUseCase;

  @PostMapping("/learn")
  void startLearning(FileRequest fileRequest) {
    log.info("Received learn request = {}", fileRequest);
    handleLearningAlertsUseCase.readAlerts(fileRequest);
  }
}
