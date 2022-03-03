package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RecordCompositeReaderConfiguration {

  private final ScbBridgeConfigProperties properties;

  @Bean(destroyMethod = "")
  @JobScope
  RecordCompositeReader alertLevelRecordCompositeReader(
      ExternalSystemIdsReaderFactory alertLevelSystemIdsReaderFactory,
      MultipleAlertCompositeFetcher alertLevelAlertCompositeFetcher) {

    return createRecordCompositeReader(
        alertLevelSystemIdsReaderFactory, alertLevelAlertCompositeFetcher);
  }

  @Bean(destroyMethod = "")
  @JobScope
  RecordCompositeReader watchlistLevelRecordCompositeReader(
      ExternalSystemIdsReaderFactory watchlistLevelSystemIdsReaderFactory,
      MultipleAlertCompositeFetcher watchlistLevelAlertCompositeFetcher) {

    return createRecordCompositeReader(
        watchlistLevelSystemIdsReaderFactory, watchlistLevelAlertCompositeFetcher);
  }

  @Bean(destroyMethod = "")
  @JobScope
  RecordCompositeReader learningAlertLevelRecordCompositeReader(
      ExternalSystemIdsReaderFactory alertLevelLearningSystemIdsReaderFactory,
      MultipleAlertCompositeFetcher learningAlertLevelCompositeFetcher) {

    return createRecordCompositeReader(
        alertLevelLearningSystemIdsReaderFactory, learningAlertLevelCompositeFetcher);
  }

  @Bean(destroyMethod = "")
  @JobScope
  RecordCompositeReader learningWatchlistLevelRecordCompositeReader(
      ExternalSystemIdsReaderFactory watchlistLevelLearningSystemIdsReaderFactory,
      MultipleAlertCompositeFetcher learningWatchlistLevelCompositeFetcher) {

    return createRecordCompositeReader(
        watchlistLevelLearningSystemIdsReaderFactory, learningWatchlistLevelCompositeFetcher);
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
