package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.svb.newlearning.domain.LearningRegisteredAlert;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertIdSet;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexRegisteredAlert;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ProcessRegisteredService {

  private final IndexLearningUseCase indexLearningUseCase;
  private final IndexAnalystDecisionHelper indexAnalystDecisionHelper;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  LearningRegisteredAlert process(AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    indexLearningUseCase.indexForLearning(
        createIndexRegisteredAlerts(alertComposite, registeredAlert));
    return alertComposite.toLearningRegisteredAlert(registeredAlert);
  }

  private List<IndexRegisteredAlert> createIndexRegisteredAlerts(
      AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    return List.of(new IndexRegisteredAlert(
        new IndexAlertIdSet(
            String.valueOf(alertComposite.getAlertDetails().getAlertId()),
            registeredAlert.getAlertName(),
            alertComposite.getSystemId(),
            alertComposite.getAlertDetails().getMessageId()),
        registeredAlert.getMatches()
            .stream()
            .map(RegisteredMatch::getMatchName)
            .collect(Collectors.toList()),
        indexAnalystDecisionHelper.getDecision(alertComposite.getActions())));
  }

}
