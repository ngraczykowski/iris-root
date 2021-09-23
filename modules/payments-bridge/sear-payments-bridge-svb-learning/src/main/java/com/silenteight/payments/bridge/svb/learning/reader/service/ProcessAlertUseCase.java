package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.categories.port.incoming.CreateCategoryValuesUseCase;
import com.silenteight.payments.bridge.svb.learning.features.port.incoming.CreateFeaturesUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class ProcessAlertUseCase {

  private final CreateFeaturesUseCase createFeaturesUseCase;
  private final CreateCategoryValuesUseCase createCategoryValuesUseCase;
  private final RegisterAlertUseCase registerAlertUseCase;

  void processAlert(LearningAlert learningAlert) {

    var response = registerAlertUseCase.register(List.of(learningAlert.toRegisterAlertRequest()));
    learningAlert.setAlertMatchNames(response.get(0));

    createFeaturesUseCase.createMatchFeatures(learningAlert);
    createCategoryValuesUseCase.createCategoryValues(learningAlert);
  }
}
