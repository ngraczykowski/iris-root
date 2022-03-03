package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.customerbridge.common.recommendation.RecommendationOrderProperties;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class SingleAlertFetcherConfiguration {

  private final ScbBridgeConfigProperties configProperties;
  private final RecommendationOrderProperties recommendationOrderProperties;
  private final ApplicationEventPublisher eventPublisher;
  private final GnsSolutionMapper gnsSolutionMapper;

  @Bean
  SingleAlertFetcher recommendationAlertFetcher(
      @Qualifier("externalOnDemandDataSource") DataSource dataSource) {
    AlertFetcherConfigurationHelper configurationHelper =
        new AlertFetcherConfigurationHelper(configProperties, eventPublisher, gnsSolutionMapper);

    RecordDecisionsFetcher decisionsFetcher =
        configurationHelper.createDecisionsFetcher(
            recommendationOrderProperties.getDbRelationName());
    RecordCompositeFetcher recordCompositeFetcher =
        configurationHelper.createRecordCompositeFetcher(
            recommendationOrderProperties.getDbRelationName(),
            recommendationOrderProperties.getCbsHitsDetailsHelperViewName(),
            false);

    return new DatabaseSingleAlertFetcher(
        decisionsFetcher,
        recordCompositeFetcher,
        dataSource);
  }
}
