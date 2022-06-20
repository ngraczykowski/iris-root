/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.health;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.quartz.QueuingJobsProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.config.SyncDataSourcesConfiguration;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnLearningAlertCondition;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnLearningEcmCondition;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnOracleCondition;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnQueuingJobsCondition;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Import(SyncDataSourcesConfiguration.class)
@Conditional(OnOracleCondition.class)
public class HealthIndicatorConfiguration {

  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;
  private final EcmBridgeLearningJobProperties ecmBridgeLearningJobProperties;
  private final QueuingJobsProperties queuingJobsProperties;

  @Bean
  @Conditional(OnQueuingJobsCondition.class)
  HealthIndicator databaseQueuingJobsConsistencyHealthIndicator(
      @Qualifier("externalDataSource") DataSource externalDataSource) {
    return new DatabaseQueuingJobsConsistencyHealthIndicator(
        externalDataSource, queuingJobsProperties);
  }

  @Bean
  @Conditional({ OnLearningEcmCondition.class, OnLearningAlertCondition.class })
  HealthIndicator databaseLearningJobsConsistencyHealthIndicator(
      @Qualifier("externalDataSource") DataSource externalDataSource) {
    return new DatabaseLearningJobsConsistencyHealthIndicator(
        externalDataSource,
        alertLevelLearningJobProperties,
        ecmBridgeLearningJobProperties);
  }
}
