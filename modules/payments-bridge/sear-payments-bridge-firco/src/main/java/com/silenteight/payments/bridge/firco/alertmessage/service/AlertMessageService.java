package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertData;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageUseCase;
import com.silenteight.sep.base.common.exception.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
class AlertMessageService implements AlertMessageUseCase {

  private final AlertMessageRepository alertMessageRepository;

  public AlertData findByAlertMessageId(UUID alertMessageId) {
    var entity = alertMessageRepository.findById(alertMessageId)
        .orElseThrow(EntityNotFoundException::new);

    return AlertData.builder()
        .messageId(entity.getMessageId())
        .businessUnit(entity.getBusinessUnit())
        .dataCenter(entity.getDataCenter())
        .decisionUrl(entity.getDecisionUrl())
        .priority(entity.getPriority())
        .receivedAt(entity.getReceivedAt())
        .systemId(entity.getSystemId())
        .alertId(entity.getId())
        .unit(entity.getUnit())
        .numberOfHits(entity.getNumberOfHits())
        .userLogin(entity.getUserLogin())
        .userPassword(entity.getUserPassword())
        .build();
  }

  @Override
  @Transactional
  public void delete(List<UUID> alertMessageIds) {

    log.info("Deleting alert message: alertsCount={}, alerts={}",
        alertMessageIds.size(), alertMessageIds);

    int alertMessagesDeletedCount = alertMessageRepository.deleteAllByIdIn(alertMessageIds);

    log.info("Alert message removed, count={}", alertMessagesDeletedCount);

  }

  @Override
  public long findReceivedAtTimeMilli(UUID alertMessageId) {
    var entity = alertMessageRepository
        .findAlertMessageEntitiesByIdProjected(alertMessageId)
        .orElseThrow(() -> new EntityNotFoundException(
            "Cannot find receivedAt for alertMessageId: " + alertMessageId));

    return entity.getReceivedAt().getTime();
  }

}
