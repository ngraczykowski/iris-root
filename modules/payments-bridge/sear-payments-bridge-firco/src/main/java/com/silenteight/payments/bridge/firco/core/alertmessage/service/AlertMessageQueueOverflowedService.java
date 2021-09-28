package com.silenteight.payments.bridge.firco.core.alertmessage.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.STORED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@Slf4j
@Component
class AlertMessageQueueOverflowedService {

  private final AlertMessageStatusRepository repository;
  private final AlertMessageStatusService alertMessageStatusService;
  private final ResponseGeneratorService responseGeneratorService;

  private final AlertMessageProperties alertMessageProperties;

  boolean resolve(AlertMessageModel message) {
    if (repository.countAllByStatus(STORED) >
        alertMessageProperties.getStoredQueueLimit()) {
      log.debug("AlertMessage [{}] rejected due to queue limit ({})",
          message.getId(), alertMessageProperties.getStoredQueueLimit());

      responseGeneratorService.prepareAndSendResponse(message.getId(), REJECTED_OVERFLOWED);
      alertMessageStatusService.transitionAlertMessageStatus(
          message.getId(), REJECTED_OVERFLOWED);
      return true;
    }
    return false;
  }

}
