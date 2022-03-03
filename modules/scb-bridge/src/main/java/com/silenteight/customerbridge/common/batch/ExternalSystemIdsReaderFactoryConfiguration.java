package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.customerbridge.common.quartz.ScbBridgeAlertLevelLearningJobProperties;
import com.silenteight.customerbridge.common.quartz.ScbBridgeAlertLevelSolvingJobProperties;
import com.silenteight.customerbridge.common.quartz.ScbBridgeWatchlistLevelLearningJobProperties;
import com.silenteight.customerbridge.common.quartz.ScbBridgeWatchlistLevelSolvingJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
class ExternalSystemIdsReaderFactoryConfiguration {

  private final ScbBridgeConfigProperties properties;
  private final ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingJobProperties;
  private final ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties;

  @Bean
  @JobScope
  ExternalSystemIdsReaderFactory alertLevelSystemIdsReaderFactory(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    return createReaderFactory(
        externalDataSource, alertLevelSolvingJobProperties.getDbRelationName());
  }

  @Bean
  @JobScope
  ExternalSystemIdsReaderFactory watchlistLevelSystemIdsReaderFactory(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    return createReaderFactory(
        externalDataSource, watchlistLevelSolvingJobProperties.getDbRelationName());
  }

  @Bean
  @JobScope
  ExternalSystemIdsReaderFactory alertLevelLearningSystemIdsReaderFactory(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    return createReaderFactory(
        externalDataSource, alertLevelLearningJobProperties.getDbRelationName());
  }

  @Bean
  @JobScope
  ExternalSystemIdsReaderFactory watchlistLevelLearningSystemIdsReaderFactory(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    return createReaderFactory(
        externalDataSource, watchlistLevelLearningJobProperties.getDbRelationName());
  }

  private ExternalSystemIdsReaderFactory createReaderFactory(
      DataSource dataSource, String dbRelationName) {
    return new ExternalSystemIdsReaderFactory(
        dataSource,
        dbRelationName,
        properties.getChunkSize(),
        properties.getQueryTimeout());
  }
}
