package com.silenteight.adjudication.engine.dataset.dataset.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.alerts.alert.AlertFacade;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.adjudication.engine.dataset.dataset.DatasetFacade;
import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.dataretention.api.v1.DatasetsExpired;

import org.springframework.integration.dsl.IntegrationFlowAdapter;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.adjudication.engine.dataset.dataset.integration.DataRetentionChannels.ALERTS_EXPIRED_INBOUND_CHANNEL;
import static com.silenteight.adjudication.engine.dataset.dataset.integration.DataRetentionChannels.ALERTS_EXPIRED_OUTBOUND_CHANNEL;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Component
@Slf4j
class ReceiveExpiredAlertsIntegrationFlow extends IntegrationFlowAdapter {

  private final DatasetFacade datasetFacade;
  private final AlertFacade alertFacade;

  @Override
  protected IntegrationFlowDefinition<?> buildFlow() {
    return from(ALERTS_EXPIRED_INBOUND_CHANNEL)
        .handle(AlertsExpired.class, this::handleResponse)
        .channel(ALERTS_EXPIRED_OUTBOUND_CHANNEL);
  }

  private DatasetsExpired handleResponse(AlertsExpired payload, MessageHeaders headers) {

    List<String> alertNames = payload.getAlertsList();

    log.debug("Received expired alerts = {}", alertNames);

    var alertIds = alertNames.stream()
        .map(a -> ResourceName.create(a).getLong("alerts"))
        .collect(toList());

    var expiredDatasets = datasetFacade.getDatasetsByAlerts(alertNames);

    alertFacade.deleteAlerts(alertIds);

    if (log.isDebugEnabled()) {
      log.debug("Sending expired datasets = {}", expiredDatasets);
    }
    return DatasetsExpired.newBuilder().addAllDatasets(expiredDatasets).build();
  }
}
