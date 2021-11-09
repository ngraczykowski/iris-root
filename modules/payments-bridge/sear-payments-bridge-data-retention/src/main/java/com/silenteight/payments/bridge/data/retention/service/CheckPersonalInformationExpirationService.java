package com.silenteight.payments.bridge.data.retention.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.PersonalInformationExpired;
import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.DataType;
import com.silenteight.payments.bridge.data.retention.port.CheckPersonalInformationExpirationUseCase;
import com.silenteight.payments.bridge.data.retention.port.SendPersonalInformationExpiredPort;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableConfigurationProperties(DataRetentionProperties.class)
@Slf4j
class CheckPersonalInformationExpirationService extends DataExpirationTemplate implements
    CheckPersonalInformationExpirationUseCase {

  private final DataRetentionProperties properties;
  private final SendPersonalInformationExpiredPort sendPersonalInformationExpiredPort;

  public CheckPersonalInformationExpirationService(
      DataRetentionProperties properties,
      AlertDataRetentionAccessPort alertDataRetentionAccessPort,
      SendPersonalInformationExpiredPort sendPersonalInformationExpiredPort) {
    super(alertDataRetentionAccessPort);
    this.properties = properties;
    this.sendPersonalInformationExpiredPort = sendPersonalInformationExpiredPort;
  }

  @Override
  @Transactional
  public void execute() {
    doExecute(properties.getPersonalInformation().getExpiration(), DataType.PERSONAL_INFORMATION);
  }

  protected void sendMessage(List<String> alertNames) {
    log.info("The personal identifiable information data of the following alerts is being "
        + "requested to be deleted in accordance with the retention policy: [{}]", alertNames);
    sendPersonalInformationExpiredPort.send(
        PersonalInformationExpired.newBuilder()
            .addAllAlerts(alertNames)
            .build());
  }

}
