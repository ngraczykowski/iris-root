package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand;
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FedMatch;
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FeedingStatus;
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertMatchesFeatureInputFedReceivedAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(
      queues = "${amqp.registration.incoming.match-feature-input-set-fed.queue-name}",
      containerFactory = "registrationRabbitAmqpListenerContainerFactory",
      errorHandler = "registrationAmqpErrorHandler"
  )
  public void matchFeatureInputSetFed(List<MessageAlertMatchesFeatureInputFed> messages) {
    log.info("Received {} messages with alerts. Trying to add them to analysis", messages.size());
    var commands = messages.stream()
        .map(this::createCommand)
        .toList();
    registrationFacade.addAlertsToAnalysis(commands);
  }

  private AddAlertToAnalysisCommand createCommand(MessageAlertMatchesFeatureInputFed message) {
    return AddAlertToAnalysisCommand.builder()
        .batchId(message.getBatchId())
        .alertId(message.getAlertId())
        .errorDescription(message.getAlertErrorDescription())
        .feedingStatus(FeedingStatus.valueOf(message.getFeedingStatus().name()))
        .fedMatches(message.getFedMatchesList().stream()
            .map(fedMatch -> new FedMatch(fedMatch.getMatchId()))
            .toList())
        .build();
  }
}
