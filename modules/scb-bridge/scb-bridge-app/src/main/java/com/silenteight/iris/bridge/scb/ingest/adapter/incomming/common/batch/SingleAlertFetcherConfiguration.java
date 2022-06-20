/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation.RecommendationOrderProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord.GnsSolutionMapper;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnLearningAlertCondition;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Conditional(OnLearningAlertCondition.class)
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
