package com.silenteight.adjudication.engine.dataset.dataset.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.dataset.dataset.DatasetFacade;
import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.DatasetsExpired;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.silenteight.adjudication.engine.dataset.dataset.integration.DataRetentionChannels.ALERTS_EXPIRED_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.dataset.dataset.integration.DataRetentionChannels.ALERTS_EXPIRED_OUTBOUND_CHANNEL;

@RequiredArgsConstructor
@Component
@Slf4j
class ReceiveExpiredAlertsIntegrationFlow extends IntegrationFlowAdapter {

  private final DatasetFacade facade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERTS_EXPIRED_INBOUND_CHANNEL)
        .handle(Object.class, this::handleResponse)
        .channel(ALERTS_EXPIRED_OUTBOUND_CHANNEL);
  }

  private Object handleResponse(Object payload, MessageHeaders headers) {
    AlertsExpired alerts;
    try {
      alerts = ((Any) payload).unpack(AlertsExpired.class);
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalArgumentException();
    }
    log.debug("Received expired alerts. alerts count = {}", alerts.getAlertsCount());
    var result = facade.getDatasetsByAlerts(alerts.getAlertsList());
    return DatasetsExpired.newBuilder().addAllDatasets(result).build();
  }
}
