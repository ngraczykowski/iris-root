package com.silenteight.payments.bridge.firco.core.alertmessage.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.FircoAlertMessage;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.RECEIVED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Slf4j
@Component
class AlertMessageQueueOverflowedService {

  private final AlertMessageStatusRepository repository;
  private final TransitionAlertMessageStatusService transitionAlertMessageStatusService;
  private final ResponseGeneratorService responseGeneratorService;

  private final AlertMessageProperties alertMessageProperties;

  boolean resolve(FircoAlertMessage message) {
    if (repository.countAllByStatus(RECEIVED) >
        alertMessageProperties.getQueueMessageStoredLimit()) {
      log.debug("AlertMessage [{}] rejected due to queue limit ({})",
          message.getId(), alertMessageProperties.getQueueMessageStoredLimit());

      responseGeneratorService.prepareAndSendResponse(message.getId(), REJECTED_OVERFLOWED);
      transitionAlertMessageStatusService.transitionAlertMessageStatus(
          message.getId(), REJECTED_OVERFLOWED);
      return true;
    }
    return false;
  }

}
