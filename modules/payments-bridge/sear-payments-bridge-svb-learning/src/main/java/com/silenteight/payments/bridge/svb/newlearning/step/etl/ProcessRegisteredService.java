package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.RegisteredAlert;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningRegisteredAlert;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ProcessRegisteredService {

  LearningRegisteredAlert process(AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    return alertComposite.toLearningRegisteredAlert(registeredAlert);
  }
}
