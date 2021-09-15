package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.features.port.incoming.CreateFeaturesUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.ReadAlertsUseCase;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReadAlertsService implements ReadAlertsUseCase {

  private final LearningCsvReader learningCsvReader;
  private final CreateFeaturesUseCase createFeaturesUseCase;
  private final CreateCategoryValuesUseCase createCategoryValuesUseCase;

  public void readAlerts() {
    learningCsvReader.read(this::processAlert);
  }

  void processAlert(LearningAlert learningAlert) {
    createFeaturesUseCase.createMatchFeatures(learningAlert);
    createCategoryValuesUseCase.createCategoryValues(learningAlert);
  }
}
