package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeWatchlistLevelLearningJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
class MultipleAlertFetcherLearningConfiguration extends MultipleAlertFetcherConfiguration {

  private final ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties;
  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;

  MultipleAlertFetcherLearningConfiguration(
      ScbBridgeWatchlistLevelLearningJobProperties watchlistLevelLearningJobProperties,
      ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties,
      ScbBridgeConfigProperties configProperties,
      ApplicationEventPublisher eventPublisher,
      GnsSolutionMapper gnsSolutionMapper) {
    super(configProperties, eventPublisher, gnsSolutionMapper);
    this.watchlistLevelLearningJobProperties = watchlistLevelLearningJobProperties;
    this.alertLevelLearningJobProperties = alertLevelLearningJobProperties;
  }

  @Bean
  @JobScope
  MultipleAlertCompositeFetcher learningAlertLevelCompositeFetcher(
      @Qualifier("externalDataSource") DataSource dataSource, GnsSyncDeltaService deltaService) {

    RecordCompositeFetcher recordCompositeFetcher =
        configurationHelper.createRecordCompositeFetcher(
            alertLevelLearningJobProperties.getDbRelationName(),
            alertLevelLearningJobProperties.getCbsHitsDetailsHelperViewName(),
            alertLevelLearningJobProperties.isWatchlistLevel());

    if (alertLevelLearningJobProperties.isUseDelta()) {
      return DeltaAlertCompositeFetcher
          .builder()
          .recordCompositeFetcher(recordCompositeFetcher)
          .syncDeltaService(deltaService)
          .deltaJobName(alertLevelLearningJobProperties.getDeltaJobName())
          .externalDataSource(dataSource)
          .decisionsFetcher(configurationHelper.createDecisionsFetcher(
              alertLevelLearningJobProperties.getDbRelationName()))
          .build();
    } else {
      return new DatabaseMultipleAlertCompositeFetcher(
          configurationHelper.createDecisionsFetcher(
              alertLevelLearningJobProperties.getDbRelationName()), dataSource,
          recordCompositeFetcher);
    }
  }

  @Bean
  @JobScope
  MultipleAlertCompositeFetcher learningWatchlistLevelCompositeFetcher(
      @Qualifier("externalDataSource") DataSource dataSource, GnsSyncDeltaService deltaService) {

    RecordDecisionsFetcher decisionsFetcher =
        configurationHelper.createDecisionsFetcher(
            watchlistLevelLearningJobProperties.getDbRelationName());
    RecordCompositeFetcher recordCompositeFetcher =
        configurationHelper.createRecordCompositeFetcher(
            watchlistLevelLearningJobProperties.getDbRelationName(),
            watchlistLevelLearningJobProperties.getCbsHitsDetailsHelperViewName(),
            watchlistLevelLearningJobProperties.isWatchlistLevel());

    if (watchlistLevelLearningJobProperties.isUseDelta()) {
      return DeltaAlertCompositeFetcher
          .builder()
          .recordCompositeFetcher(recordCompositeFetcher)
          .syncDeltaService(deltaService)
          .deltaJobName(watchlistLevelLearningJobProperties.getDeltaJobName())
          .externalDataSource(dataSource)
          .decisionsFetcher(decisionsFetcher)
          .build();
    } else {
      return new DatabaseMultipleAlertCompositeFetcher(
          decisionsFetcher, dataSource, recordCompositeFetcher);
    }
  }
}
