package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.features.port.incoming.CreateFeaturesUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class DataSourceIngestService {

  private final CreateFeaturesUseCase createFeaturesUseCase;
  private final CreateCategoryValuesUseCase createCategoryValuesUseCase;

  void createValues(List<LearningAlert> learningAlerts, List<ReadAlertError> errors) {
    createFeaturesUseCase.createMatchFeatures(learningAlerts, errors);
    createCategoryValuesUseCase.createCategoryValues(learningAlerts, errors);
  }
}
