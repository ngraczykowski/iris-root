package com.silenteight.payments.bridge.app.integration.learning;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;
import com.silenteight.payments.bridge.warehouse.index.model.learning.*;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class IndexLearningIntegrationService implements IndexLearningAlertPort {

  private final IndexLearningUseCase indexLearningUseCase;

  public void indexForLearning(List<IndexRegisterAlertRequest> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream()
        .map(this::buildIndexRegisteredAlert).collect(toList());
    indexLearningUseCase.indexForLearning(alerts);
  }

  private IndexRegisteredAlert buildIndexRegisteredAlert(IndexRegisterAlertRequest alert) {
    var alertIdSet = new IndexAlertIdSet(
        alert.getAlertId(), alert.getAlertName(),
        alert.getSystemId(), alert.getMessageId());
    return new IndexRegisteredAlert(
        alertIdSet, alert.getMatchNames(), buildIndexAnalystDecision(alert));
  }

  private static IndexAnalystDecision buildIndexAnalystDecision(IndexRegisterAlertRequest alert) {
    var learningAnalystDecision = alert.getAnalystDecision();
    return new IndexAnalystDecision(learningAnalystDecision.getStatus(),
        alert.getDecision(), learningAnalystDecision.getComment(),
        learningAnalystDecision.getActionDateTimeAsString());
  }

  public void index(List<LearningAlert> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream().map(this::buildIndexAlert)
        .collect(toList());
    indexLearningUseCase.index(alerts);
  }

  private IndexAlert buildIndexAlert(LearningAlert learningAlert) {
    var alertIdSet = new IndexAlertIdSet(
        learningAlert.getAlertId(), learningAlert.getAlertName(),
        learningAlert.getSystemId(), learningAlert.getMessageId());
    var matches = learningAlert.getMatches().stream()
        .map(match -> new IndexMatch(
            match.getMatchId(), match.getMatchName(),
            String.join(", ", match.getMatchingTexts())))
        .collect(toList());
    return new IndexAlert(alertIdSet, matches, buildIndexAnalystDecision(learningAlert));
  }

  private static IndexAnalystDecision buildIndexAnalystDecision(LearningAlert alert) {
    var learningAnalystDecision = alert.getAnalystDecision();
    return new IndexAnalystDecision(learningAnalystDecision.getStatus(),
        alert.getDecision(), learningAnalystDecision.getComment(),
        learningAnalystDecision.getActionDateTimeAsString());
  }

}
