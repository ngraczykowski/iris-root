package com.silenteight.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaService;
import com.silenteight.scb.ingest.adapter.incomming.common.mode.OnLearningEcmCondition;
import com.silenteight.scb.ingest.adapter.incomming.common.quartz.EcmBridgeLearningJobProperties;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Conditional(OnLearningEcmCondition.class)
class EcmMultipleAlertFetcherConfiguration {

  private final EcmBridgeLearningJobProperties ecmLearningJobProperties;
  private final ScbBridgeConfigProperties configProperties;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  @JobScope
  EcmDeltaAlertCompositeFetcher ecmLearningAlertCompositeFetcher(
      DataSource ecmDataSource,
      @Qualifier("externalDataSource") DataSource externalDataSource,
      GnsSyncDeltaService deltaService) {

    var ecmAlertFetcherConfigurationHelper = new EcmAlertFetcherConfigurationHelper(
        configProperties, eventPublisher, ecmLearningJobProperties.getDecisions());

    EcmRecordDecisionsFetcher decisionsFetcher =
        ecmAlertFetcherConfigurationHelper.createEcmDecisionsFetcher(
            ecmLearningJobProperties.getEcmViewName());
    EcmRecordCompositeFetcher recordCompositeFetcher =
        ecmAlertFetcherConfigurationHelper.createEcmRecordCompositeFetcher(
            ecmLearningJobProperties.getDbRelationName(),
            ecmLearningJobProperties.getCbsHitsDetailsHelperViewName());

    return new EcmDeltaAlertCompositeFetcher(
        decisionsFetcher,
        ecmDataSource,
        externalDataSource,
        recordCompositeFetcher,
        deltaService,
        ecmLearningJobProperties.getDeltaJobName());
  }
}
