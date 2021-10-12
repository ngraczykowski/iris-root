package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.AddAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertsToAnalysisUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class ProcessAlertUseCase {

  private final CreateDataSourceValuesUseCase createDataSourceValuesUseCase;
  private final AddAlertsToAnalysisUseCase addAlertsToAnalysisUseCase;
  private final RegisterAlertUseCase registerAlertUseCase;

  void processAlert(LearningAlert learningAlert) {

    if (learningAlert.getMatches().size() == 0)
      return;

    var response = registerAlertUseCase.register(List.of(learningAlert.toRegisterAlertRequest()));
    learningAlert.setAlertMatchNames(response.get(0));

    createDataSourceValuesUseCase.createValues(learningAlert);

    addAlertsToAnalysisUseCase.addAlerts(
        AddAlertRequest.fromAlertNames(List.of(learningAlert.getAlertName())));
  }
}
