package com.silenteight.scb.ingest.adapter.incomming.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertData;
import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.scb.ingest.domain.DataRetentionService;
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand;
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand.DataRetentionAlert;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
class DataRetentionEventListener {

  private final DataRetentionService dataRetentionService;

  @RabbitListener(
      queues = "${amqp.ingest.incoming.data-retention.queue-name}",
      errorHandler = "dataRetentionAmqpErrorHandler")
  public void receiveMessage(AlertsExpired message) {
    log.info("Received {} alerts to perform data retention", message.getAlertsCount());
    var command = createCommand(message);
    dataRetentionService.performDataRetention(command);
  }

  private DataRetentionCommand createCommand(AlertsExpired message) {
    var alerts = createDataRetentionAlerts(message.getAlertsDataList());
    return new DataRetentionCommand(alerts);
  }

  private List<DataRetentionAlert> createDataRetentionAlerts(List<AlertData> messageAlerts) {
    return messageAlerts.stream()
        .map(alert -> DataRetentionCommand.DataRetentionAlert.builder()
            .internalBatchId(alert.getBatchId())
            .systemId(alert.getAlertId())
            .build())
        .toList();
  }
}
