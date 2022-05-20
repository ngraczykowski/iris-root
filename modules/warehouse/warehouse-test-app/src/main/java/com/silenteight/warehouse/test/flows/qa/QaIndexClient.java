package com.silenteight.warehouse.test.flows.qa;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.QaDataIndexRequest;
import com.silenteight.warehouse.test.client.gateway.QaIndexClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class QaIndexClient {

  private static final int ALERTS_LIMIT = 1;

  @NonNull
  private final QaIndexClientGateway qaIndexClientGateway;
  @NonNull
  private final AlertGenerator alertGenerator;

  @Scheduled(cron = "${test.generator.cron}")
  void init() {
    String requestId = randomUUID().toString();
    String analysisName = "analysis/" + randomUUID();

    QaDataIndexRequest request = QaDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .addAllAlerts(alertGenerator.generateQa(ALERTS_LIMIT)).build();

    qaIndexClientGateway.indexRequest(request);
    log.info("QA msg sent, requestId={}, analysis={}", requestId, analysisName);
  }
}
