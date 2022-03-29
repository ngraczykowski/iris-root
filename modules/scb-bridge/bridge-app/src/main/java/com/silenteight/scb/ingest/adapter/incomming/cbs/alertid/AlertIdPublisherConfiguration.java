package com.silenteight.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestBatchEventPublisher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AlertIdPublisherConfiguration {

  private final AlertInFlightService alertInFlightService;
  private final IngestBatchEventPublisher ingestBatchEventPublisher;
  private final BatchInfoService batchInfoService;

  @Bean
  AlertIdPublisher alertIdPublisher() {
    return AlertIdPublisher.builder()
        .alertInFlightService(alertInFlightService)
        .ingestBatchEventPublisher(ingestBatchEventPublisher)
        .batchInfoService(batchInfoService)
        .build();
  }
}
