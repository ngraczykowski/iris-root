package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.features.port.incoming.CreateFeaturesUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DataSourceIngestService {

  private final CreateFeaturesUseCase createFeaturesUseCase;
  private final CreateCategoryValuesUseCase createCategoryValuesUseCase;

  void createValues(LearningAlert learningAlert) {
    createFeaturesUseCase.createMatchFeatures(learningAlert);
    createCategoryValuesUseCase.createCategoryValues(learningAlert);
  }
}
