package com.silenteight.warehouse.test.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.warehouse.indexer.query.single.AlertSearchCriteria;
import com.silenteight.warehouse.indexer.query.single.RandomAlertService;
import com.silenteight.warehouse.test.client.gateway.QaIndexClientGateway;
import com.silenteight.warehouse.test.flows.production.v2.AlertGenerator;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class QaIndexClient {

  private static final Instant ALERTS_DATE_TO = Instant.now();
  private static final Instant ALERTS_DATE_FROM = ALERTS_DATE_TO.minus(90, DAYS);
  private static final int ALERTS_LIMIT = 1;
  private static final String TIME_FIELD_NAME = "alert_recommendationDate";

  @NonNull
  private final QaIndexClientGateway qaIndexClientGateway;
  @NonNull
  private final AlertGenerator alertGenerator;
  @NonNull
  private final RandomAlertService randomAlertQueryService;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();
    List<String> alertNames = getRandomAlertNames();
    assertDiscriminatorsNotEmpty(alertNames);
    qaIndexClientGateway.indexRequest(getQaDataIndexRequest(alertNames, requestId));
    log.info("QaDataIndexRequest msg sent, requestId={}", requestId);
  }

  private QaDataIndexRequest getQaDataIndexRequest(List<String> alertNames, String requestId) {
    return QaDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .addAllAlerts(getAlerts(alertNames))
        .build();
  }

  private List<QaAlert> getAlerts(List<String> alertNames) {
    return alertNames.stream()
        .map(alertGenerator::generateQa)
        .collect(toList());
  }

  private List<String> getRandomAlertNames() {
    return randomAlertQueryService.getRandomAlertNameByCriteria(
        AlertSearchCriteria
            .builder()
            .timeFieldName(TIME_FIELD_NAME)
            .alertLimit(ALERTS_LIMIT)
            .timeRangeFrom(ALERTS_DATE_FROM.toString())
            .timeRangeTo(ALERTS_DATE_TO.toString())
            .filter(emptyList())
            .build());
  }

  private void assertDiscriminatorsNotEmpty(List<String> discriminators) {
    if (discriminators.isEmpty())
      throw new AlertsNotFoundException(ALERTS_DATE_FROM, ALERTS_DATE_TO);
  }
}
