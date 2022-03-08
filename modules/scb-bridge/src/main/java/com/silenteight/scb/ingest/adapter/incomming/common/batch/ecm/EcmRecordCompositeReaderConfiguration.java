package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class EcmRecordCompositeReaderConfiguration {

  private final ScbBridgeConfigProperties properties;

  @Bean(destroyMethod = "")
  @JobScope
  EcmRecordCompositeReader ecmLearningRecordCompositeReader(
      ExternalIdsReaderFactory externalIdsReaderFactory,
      EcmDeltaAlertCompositeFetcher ecmLearningAlertCompositeFetcher) {

    return new EcmRecordCompositeReader(
        externalIdsReaderFactory.get(),
        ecmLearningAlertCompositeFetcher,
        properties.getOraclePageSize());
  }
}
