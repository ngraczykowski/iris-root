package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningRegisteredAlert;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ProcessRegisteredService {

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  LearningRegisteredAlert process(AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    return alertComposite.toLearningRegisteredAlert(registeredAlert);
  }
}
