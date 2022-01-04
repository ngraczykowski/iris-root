package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningRegisteredAlert;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class ProcessUnregisteredService {

  private final RegisterAlertUseCase registerAlertUseCase;

  LearningRegisteredAlert process(AlertComposite alertComposite, long jobId) {
    var registeredAlerts = registerAlertUseCase.batchRegistration(
        List.of(alertComposite.toRegisterAlertRequest(jobId)));
    var registeredAlert = registeredAlerts.get(0);
    return alertComposite.toLearningRegisteredAlert(registeredAlert);
  }
}
