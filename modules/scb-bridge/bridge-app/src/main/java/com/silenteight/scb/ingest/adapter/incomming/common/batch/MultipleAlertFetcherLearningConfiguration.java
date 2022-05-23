package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;
import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.ScbBridgeAlertLevelLearningJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Conditional(OnAlertProcessorCondition.class)
class MultipleAlertFetcherLearningConfiguration extends MultipleAlertFetcherConfiguration {

  private final ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties;

  MultipleAlertFetcherLearningConfiguration(
      ScbBridgeAlertLevelLearningJobProperties alertLevelLearningJobProperties,
      ScbBridgeConfigProperties configProperties,
      ApplicationEventPublisher eventPublisher,
      GnsSolutionMapper gnsSolutionMapper) {
    super(configProperties, eventPublisher, gnsSolutionMapper);
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
}
