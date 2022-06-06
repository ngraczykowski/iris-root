package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnLearningAlertCondition;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@Conditional(OnLearningAlertCondition.class)
class ExternalSystemIdsReaderFactoryConfiguration {

  private final ScbBridgeConfigProperties properties;
  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;

  @Bean
  @JobScope
  ExternalSystemIdsReaderFactory alertLevelLearningSystemIdsReaderFactory(
      @Qualifier("externalDataSource") DataSource externalDataSource) {

    return createReaderFactory(
        externalDataSource, alertLevelLearningJobProperties.getDbRelationName());
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
