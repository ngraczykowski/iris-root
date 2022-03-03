package com.silenteight.customerbridge.common.batch;

import com.silenteight.customerbridge.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.customerbridge.common.quartz.ScbBridgeAlertLevelSolvingJobProperties;
import com.silenteight.customerbridge.common.quartz.ScbBridgeWatchlistLevelSolvingJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
class MultipleAlertFetcherSolvingConfiguration extends MultipleAlertFetcherConfiguration {

  private final ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingJobProperties;
  private final ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties;

  MultipleAlertFetcherSolvingConfiguration(
      ScbBridgeAlertLevelSolvingJobProperties alertLevelSolvingJobProperties,
      ScbBridgeWatchlistLevelSolvingJobProperties watchlistLevelSolvingJobProperties,
      ScbBridgeConfigProperties configProperties,
      ApplicationEventPublisher eventPublisher,
      GnsSolutionMapper gnsSolutionMapper) {
    super(configProperties, eventPublisher, gnsSolutionMapper);
    this.alertLevelSolvingJobProperties = alertLevelSolvingJobProperties;
    this.watchlistLevelSolvingJobProperties = watchlistLevelSolvingJobProperties;
  }

  @Bean
  @JobScope
  MultipleAlertCompositeFetcher alertLevelAlertCompositeFetcher(
      @Qualifier("externalDataSource") DataSource dataSource) {

    RecordDecisionsFetcher decisionsFetcher =
        configurationHelper.createDecisionsFetcher(
            alertLevelSolvingJobProperties.getDbRelationName());
    RecordCompositeFetcher recordCompositeFetcher =
        configurationHelper.createRecordCompositeFetcher(
            alertLevelSolvingJobProperties.getDbRelationName(),
            alertLevelSolvingJobProperties.getCbsHitsDetailsHelperViewName(),
            false);

    return new DatabaseMultipleAlertCompositeFetcher(
        decisionsFetcher, dataSource, recordCompositeFetcher);
  }

  @Bean
  @JobScope
  MultipleAlertCompositeFetcher watchlistLevelAlertCompositeFetcher(
      @Qualifier("externalDataSource") DataSource dataSource) {

    RecordDecisionsFetcher decisionsFetcher =
        configurationHelper.createDecisionsFetcher(
            watchlistLevelSolvingJobProperties.getDbRelationName());
    RecordCompositeFetcher recordCompositeFetcher =
        configurationHelper.createRecordCompositeFetcher(
            watchlistLevelSolvingJobProperties.getDbRelationName(),
            watchlistLevelSolvingJobProperties.getCbsHitsDetailsHelperViewName(),
            true);

    return new DatabaseMultipleAlertCompositeFetcher(
        decisionsFetcher, dataSource, recordCompositeFetcher);
  }
}
