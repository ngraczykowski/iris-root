package com.silenteight.warehouse.test.flows.alertsexpired;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.warehouse.test.client.gateway.AlertsExpiredClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class AlertsExpiredClient {

  private final AlertsExpiredClientGateway alertsExpiredClientGateway;
  private final MessageGenerator messageGenerator;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();

    AlertsExpired alertsExpired = messageGenerator.generateAlertsExpired(generateAlertNames());
    alertsExpiredClientGateway.indexRequest(alertsExpired);
    log.info("AlertsExpired msg sent, requestId={} alerts count: {}", requestId,
        alertsExpired.getAlertsCount());
  }

  private static List<String> generateAlertNames() {
    return IntStream
        .range(0, 1000)
        .boxed()
        .map(i -> "alerts/" + i)
        .collect(toList());
  }
}
