package com.silenteight.payments.bridge.svb.newlearning.step.etl;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredAlert;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisteredMatch;
import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexAlertIdSet;
import com.silenteight.payments.bridge.warehouse.index.model.learning.IndexRegisteredAlert;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class ProcessRegisteredService {

  private final IndexLearningUseCase indexLearningUseCase;
  private final IndexAnalystDecisionHelper indexAnalystDecisionHelper;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  void process(
      AlertComposite alertComposite, List<RegisteredAlert> registeredAlert) {
    indexLearningUseCase.indexForLearning(
        createIndexRegisteredAlerts(alertComposite, registeredAlert));
  }

  private List<IndexRegisteredAlert> createIndexRegisteredAlerts(
      AlertComposite alertComposite, List<RegisteredAlert> registeredAlerts) {
    return registeredAlerts
        .stream()
        .map(ra -> createIndexRegisteredAlert(alertComposite, ra))
        .collect(toList());
  }

  private IndexRegisteredAlert createIndexRegisteredAlert(
      AlertComposite alertComposite, RegisteredAlert registeredAlert) {
    return new IndexRegisteredAlert(
        new IndexAlertIdSet(
            String.valueOf(alertComposite.getAlertDetails().getAlertId()),
            registeredAlert.getAlertName(),
            alertComposite.getSystemId(),
            alertComposite.getAlertDetails().getMessageId()),
        registeredAlert.getMatches()
            .stream()
            .map(RegisteredMatch::getMatchName)
            .collect(toList()),
        indexAnalystDecisionHelper.getDecision(alertComposite.getActions()));
  }

}
