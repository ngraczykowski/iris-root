package com.silenteight.payments.bridge.app.integration.learning;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.app.integration.ChannelFactory;
import com.silenteight.payments.bridge.svb.learning.reader.domain.IndexRegisterAlertRequest;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.warehouse.index.model.learning.*;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageChannel;

import java.util.List;

import static java.util.stream.Collectors.toList;

@MessageEndpoint
@RequiredArgsConstructor
class IndexLearningEndpoint {

  static final String INDEX_FOR_LEARNING = "IndexForLearningChannel";
  static final String INDEX = "IndexChannel";

  private final IndexLearningUseCase indexForLearning;

  @ServiceActivator(inputChannel = INDEX_FOR_LEARNING)
  void applyForLearning(List<IndexRegisterAlertRequest> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream()
        .map(this::buildIndexRegisteredAlert).collect(toList());
    indexForLearning.indexForLearning(alerts);
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

  @ServiceActivator(inputChannel = INDEX)
  void apply(List<LearningAlert> indexRegisterAlertRequest) {
    var alerts = indexRegisterAlertRequest.stream().map(this::buildIndexAlert)
        .collect(toList());
    indexForLearning.index(alerts);
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

  @Bean(INDEX_FOR_LEARNING)
  MessageChannel indexLearningChannel() {
    return ChannelFactory.createDirectChannel();
  }

  @Bean(INDEX)
  MessageChannel indexChannel() {
    return ChannelFactory.createDirectChannel();
  }
}
