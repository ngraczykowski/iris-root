package com.silenteight.warehouse.test.flows.analysisexpired;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.warehouse.test.client.gateway.AnalysisExpiredClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class AnalysisExpiredClient {

  private final AnalysisExpiredClientGateway analysisExpiredClientGateway;
  private final MessageGenerator messageGenerator;
  private final Integer analysisCount;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();
    AnalysisExpired analysisExpired = messageGenerator.generateAnalysisExpired(analysisCount);
    analysisExpiredClientGateway.indexRequest(analysisExpired);
    log.info("AnalysisExpired msg sent, requestId={} alerts count: {}", requestId,
        analysisExpired.getAnalysisCount());
  }
}
