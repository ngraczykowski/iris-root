package com.silenteight.bridge.core.registration.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.registration.domain.AddAlertToAnalysisCommand;
import com.silenteight.bridge.core.registration.domain.RegistrationFacade;
import com.silenteight.proto.registration.api.v1.FedMatch;
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class RegistrationRabbitAmqpListener {

  private final RegistrationFacade registrationFacade;

  @RabbitListener(
      queues = "${amqp.registration.incoming.match-feature-input-set-fed.queue-name}",
      containerFactory = "registrationRabbitAmqpListenerContainerFactory"
  )
  public void matchFeatureInputSetFed(List<MessageAlertMatchesFeatureInputFed> messages) {
    log.info("Received {} messages", messages.size());
    var commands = messages.stream()
        .map(this::createAddAlertToAnalysisCommand)
        .toList();
    registrationFacade.addAlertsToAnalysis(commands);
  }

  private AddAlertToAnalysisCommand createAddAlertToAnalysisCommand(
      MessageAlertMatchesFeatureInputFed message) {
    return new AddAlertToAnalysisCommand(
        message.getBatchId(),
        message.getAlertId(),
        message.getFedMatchesList().stream()
            .map(FedMatch::getMatchId)
            .collect(Collectors.toUnmodifiableSet())
    );
  }
}
