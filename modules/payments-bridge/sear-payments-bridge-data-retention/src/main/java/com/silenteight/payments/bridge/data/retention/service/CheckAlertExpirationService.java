package com.silenteight.payments.bridge.data.retention.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.DataType;
import com.silenteight.payments.bridge.data.retention.port.CheckAlertExpirationUseCase;
import com.silenteight.payments.bridge.data.retention.port.SendAlertsExpiredPort;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableConfigurationProperties(DataRetentionProperties.class)
@Slf4j
class CheckAlertExpirationService extends DataExpirationTemplate implements
    CheckAlertExpirationUseCase {

  private final DataRetentionProperties properties;
  private final SendAlertsExpiredPort sendAlertsExpiredPort;

  public CheckAlertExpirationService(
      DataRetentionProperties properties,
      AlertDataRetentionAccessPort alertDataRetentionAccessPort,
      SendAlertsExpiredPort sendAlertsExpiredPort) {
    super(alertDataRetentionAccessPort);
    this.properties = properties;
    this.sendAlertsExpiredPort = sendAlertsExpiredPort;
  }

  @Override
  @Transactional
  public void execute() {
    doExecute(properties.getAlertData().getExpiration(), DataType.ALERT_DATA);
  }

  @Override
  protected void sendMessage(List<String> alertNames) {
    log.info("The data of the following alerts is being requested to be deleted "
        + "in accordance with the retention policy: [{}]", alertNames);
    sendAlertsExpiredPort.send(
        AlertsExpired.newBuilder().addAllAlerts(alertNames).build());
  }
}
