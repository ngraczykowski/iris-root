package com.silenteight.payments.bridge.app.integration.learning;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.svb.learning.reader.port.IndexLearningAlertPort;
import com.silenteight.payments.bridge.warehouse.index.model.learning.*;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class IndexLearningIntegrationService implements IndexLearningAlertPort  {

  private final IndexLearningUseCase indexLearningUseCase;

  public void indexForLearning(List<IndexRegisterAlertRequest> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream()
        .map(this::buildIndexRegisteredAlert).collect(toList());
    indexLearningUseCase.indexForLearning(alerts);
  }

  private IndexRegisteredAlert buildIndexRegisteredAlert(IndexRegisterAlertRequest alert) {
    var learningAlert = alert.getLearningAlert();
    var alertIdSet = new IndexAlertIdSet(
        learningAlert.getAlertId(), learningAlert.getAlertName(),
        learningAlert.getSystemId(), learningAlert.getMessageId());
    var matches = learningAlert.getMatches().stream()
        .map(LearningMatch::getMatchName).collect(toList());
    return new IndexRegisteredAlert(alertIdSet, matches, buildIndexAnalystDecision(learningAlert));
  }

  private IndexAnalystDecision buildIndexAnalystDecision(LearningAlert learningAlert) {
    var learningAnalystDecision = learningAlert.getAnalystDecision();
    return new IndexAnalystDecision(learningAnalystDecision.getStatus(),
        learningAlert.getDecision(), learningAnalystDecision.getComment(),
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

}
