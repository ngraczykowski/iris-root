package com.silenteight.bridge.core.registration.adapter.incoming.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand;
import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand.FedMatch;
import com.silenteight.bridge.core.registration.domain.command.ProcessUdsFedAlertsCommand.FeedingStatus;
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
    log.info("Received [{}] messages with alerts fed in UDS.", messages.size());
    var commands = messages.stream()
        .map(this::createCommand)
        .toList();
    registrationFacade.processUdsFedAlerts(commands);
  }

  private ProcessUdsFedAlertsCommand createCommand(MessageAlertMatchesFeatureInputFed message) {
    return ProcessUdsFedAlertsCommand.builder()
        .batchId(message.getBatchId())
        .alertName(message.getAlertName())
        .errorDescription(message.getAlertErrorDescription())
        .feedingStatus(FeedingStatus.valueOf(message.getFeedingStatus().name()))
        .fedMatches(message.getFedMatchesList().stream()
            .map(fedMatch -> new FedMatch(fedMatch.getMatchName()))
            .toList())
        .build();
  }
}
