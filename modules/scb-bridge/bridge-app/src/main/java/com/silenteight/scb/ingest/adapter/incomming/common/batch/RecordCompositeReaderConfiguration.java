package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecordCompositeReaderConfiguration {

  private final ScbBridgeConfigProperties properties;

  @Bean(destroyMethod = "")
  @JobScope
  RecordCompositeReader learningAlertLevelRecordCompositeReader(
      ExternalSystemIdsReaderFactory alertLevelLearningSystemIdsReaderFactory,
      MultipleAlertCompositeFetcher learningAlertLevelCompositeFetcher) {

    return createRecordCompositeReader(
        alertLevelLearningSystemIdsReaderFactory, learningAlertLevelCompositeFetcher);
  }

  private RecordCompositeReader createRecordCompositeReader(
      ExternalSystemIdsReaderFactory idsReaderFactory,
      MultipleAlertCompositeFetcher alertCompositeFetcher) {
    return new RecordCompositeReader(
        idsReaderFactory.get(),
        alertCompositeFetcher,
        properties.getOraclePageSize());
  }
}
