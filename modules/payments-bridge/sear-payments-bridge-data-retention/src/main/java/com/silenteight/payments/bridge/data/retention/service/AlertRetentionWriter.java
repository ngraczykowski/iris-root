package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.payments.bridge.data.retention.adapter.AlertDataRetentionAccessPort;
import com.silenteight.payments.bridge.data.retention.port.SendAlertsExpiredPort;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.payments.bridge.data.retention.model.DataType.ALERT_DATA;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertRetentionWriter implements ItemWriter<String> {

  private final AlertDataRetentionAccessPort alertDataRetentionAccessPort;
  private final SendAlertsExpiredPort sendAlertsExpiredPort;

  @SuppressWarnings("unchecked")
  @Override
  public void write(List<? extends String> alertNames) throws Exception {
    alertDataRetentionAccessPort.update(
        (List<String>) alertNames, OffsetDateTime.now(), ALERT_DATA);


    log.info("The data of {} alerts is being requested to be deleted "
        + "in accordance with the retention policy", alertNames.size());

    sendAlertsExpiredPort.send(
        AlertsExpired.newBuilder().addAllAlerts((List<String>) alertNames).build());
  }
}
