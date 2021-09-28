package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.AlertStored;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;

import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_REJECTED;
import static com.silenteight.payments.bridge.common.integration.CommonChannels.ALERT_STORED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus.STORED;
import static com.silenteight.payments.bridge.firco.core.alertmessage.service.IntChannels.INT_ALERT_STORED_ACCEPTED;

@EnableConfigurationProperties(AlertMessageProperties.class)
@RequiredArgsConstructor
@MessageEndpoint
@Slf4j
class AlertStoredFilterEndpoint {

  private final AlertMessageStatusRepository repository;
  private final AlertMessageProperties alertMessageProperties;

  @Filter(inputChannel = ALERT_STORED, outputChannel = INT_ALERT_STORED_ACCEPTED,
      discardChannel = ALERT_REJECTED)
  public boolean alertStored(AlertStored alertStored) {
    if (repository.countAllByStatus(STORED) > alertMessageProperties.getStoredQueueLimit()) {
      var alertId = alertStored.getAlertModel().getId();
      log.debug("AlertMessage [{}] rejected due to queue limit ({})",
          alertId, alertMessageProperties.getStoredQueueLimit());
      return false;
    }
    return true;
  }
}
