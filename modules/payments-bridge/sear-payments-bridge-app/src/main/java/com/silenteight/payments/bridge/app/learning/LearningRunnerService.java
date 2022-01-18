package com.silenteight.payments.bridge.app.learning;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.port.HandleLearningAlertsUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.newlearning.job.csvstore.LearningJobConstants.STORE_CSV_JOB_NAME;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(LearningProperties.class)
class LearningRunnerService {

  private final LearningProperties properties;
  private final JobMaintainer jobMaintainer;
  private final HandleLearningAlertsUseCase handleLearningAlertsUseCase;

  void trigger(LearningFileEntity file) {
    if (properties.getUseNewLearning()) {
      jobMaintainer.runJobByName(STORE_CSV_JOB_NAME, file.toJobParameters());
      return;
    }

    handleLearningAlertsUseCase.readAlerts(file.toLearningRequest());
  }
}
