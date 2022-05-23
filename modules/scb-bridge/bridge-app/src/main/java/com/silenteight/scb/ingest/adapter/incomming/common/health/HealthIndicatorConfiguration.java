package com.silenteight.scb.ingest.adapter.incomming.common.health;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobsProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;
import com.silenteight.scb.ingest.adapter.incomming.common.order.ScbBridgeAlertOrderProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Import(SyncDataSourcesConfiguration.class)
@Conditional(OnAlertProcessorCondition.class)
public class HealthIndicatorConfiguration {

  private final ScbBridgeAlertOrderProperties alertOrderProperties;
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
        externalDataSource,
        alertOrderProperties);
  }

  @Bean
  HealthIndicator databaseLearningJobsConsistencyHealthIndicator(
      @Qualifier("externalDataSource") DataSource externalDataSource) {
    return new DatabaseLearningJobsConsistencyHealthIndicator(
        externalDataSource,
        alertLevelLearningJobProperties,
        ecmBridgeLearningJobProperties);
  }
}
