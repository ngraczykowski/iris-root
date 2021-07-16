package com.silenteight.serp.governance.qa.send;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.sampling.generator.dto.DecisionCreatedEvent;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class DecisionCreatedHandler {

  @NonNull
  private final SendAlertMessageUseCase sendAlertMessageUseCase;

  @EventListener(DecisionCreatedEvent.class)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(DecisionCreatedEvent decisionCreatedEvent) {
    sendAlertMessageUseCase.activate(SendAlertMessageCommand.of(
        createDecisionRequestsToAlertDtos(decisionCreatedEvent.getCreateDecisionRequests())));
  }

  private static List<AlertDto> createDecisionRequestsToAlertDtos(
      List<CreateDecisionRequest> createDecisionRequests) {

    return createDecisionRequests.stream()
        .map(DecisionCreatedHandler::createDecisionRequestToAlertDto)
        .collect(toList());
  }

  private static AlertDto createDecisionRequestToAlertDto(
      CreateDecisionRequest createDecisionRequest) {

    return AlertDto.builder()
        .discriminator(createDecisionRequest.getDiscriminator())
        .level(createDecisionRequest.getLevel())
        .state(createDecisionRequest.getState())
        .build();
  }
}
