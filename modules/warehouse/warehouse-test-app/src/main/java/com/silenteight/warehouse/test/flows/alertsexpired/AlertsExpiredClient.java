package com.silenteight.warehouse.test.flows.alertsexpired;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.warehouse.test.client.gateway.AlertsExpiredClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class AlertsExpiredClient {

  private final AlertsExpiredClientGateway alertsExpiredClientGateway;
  private final MessageGenerator messageGenerator;
  private final Integer alertsCount;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();

    AlertsExpired alertsExpired = messageGenerator.generateAlertsExpired(alertsCount);
    alertsExpiredClientGateway.indexRequest(alertsExpired);
    log.info("AlertsExpired msg sent, requestId={} alerts count: {}", requestId,
        alertsExpired.getAlertsCount());
  }
}
