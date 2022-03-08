package com.silenteight.scb.ingest.adapter.incomming.common.health;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobsProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.scb.ingest.adapter.incomming.common.order.ScbBridgeAlertOrderProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Import(SyncDataSourcesConfiguration.class)
public class HealthIndicatorConfiguration {

  private final CbsConfigProperties cbsConfigProperties;
  private final ScbBridgeAlertOrderProperties alertOrderProperties;
  private final ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingJobProperties;
  private final ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties;
  private final ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;
  private final QueuingJobsProperties queuingJobsProperties;

  @Bean
  HealthIndicator databaseQueuingJobsConsistencyHealthIndicator(
      @Qualifier("externalDataSource") DataSource externalDataSource) {
    return new DatabaseQueuingJobsConsistencyHealthIndicator(
        externalDataSource, queuingJobsProperties);
  }

  @Bean
  HealthIndicator databaseSolvingJobsConsistencyHealthIndicator(
      @Qualifier("externalDataSource") DataSource externalDataSource) {
    return new DatabaseSolvingJobsConsistencyHealthIndicator(
        cbsConfigProperties,
        externalDataSource,
        alertOrderProperties,
        alertLevelSolvingJobProperties,
        watchlistLevelSolvingJobProperties);
  }

  @Bean
  HealthIndicator databaseLearningJobsConsistencyHealthIndicator(
      @Qualifier("externalDataSource") DataSource externalDataSource) {
    return new DatabaseLearningJobsConsistencyHealthIndicator(
        externalDataSource,
        watchlistLevelLearningJobProperties,
        alertLevelLearningJobProperties,
        ecmBridgeLearningJobProperties);
  }
}
