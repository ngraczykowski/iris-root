package com.silenteight.payments.bridge.data.retention.service;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.model.DataType;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
abstract class DataExpirationTemplate {

  private final AlertDataRetentionAccessPort alertDataRetentionAccessPort;

  protected DataExpirationTemplate(
      AlertDataRetentionAccessPort alertDataRetentionAccessPort) {
    this.alertDataRetentionAccessPort = alertDataRetentionAccessPort;
  }

  protected void doExecute(Duration expiration, DataType dataType) {
    var dateTime = OffsetDateTime.now().minus(expiration);
    var alertNames = alertDataRetentionAccessPort.findAlertNameByAlertTimeBefore(
        dateTime, dataType);
    if (!alertNames.isEmpty()) {
      sendMessage(alertNames);
      alertDataRetentionAccessPort.update(alertNames, OffsetDateTime.now(), dataType);
    }
  }

  protected abstract void sendMessage(List<String> alertNames);

}
