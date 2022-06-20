/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsConfigProperties;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.mode.OnAlertProcessorCondition;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableConfigurationProperties({
    CbsRecommendationProperties.class, QcoRecommendationProperties.class })
@Configuration
public class CbsRecommendationServiceConfiguration {

  private final ScbBridgeConfigProperties scbBridgeConfigProperties;
  private final CbsConfigProperties cbsConfigProperties;
  private final ApplicationEventPublisher eventPublisher;
  private final AlertInFlightService alertInFlightService;

  @Bean
  @Conditional(OnAlertProcessorCondition.class)
  CbsRecommendationService cbsRecommendationService(
      CbsRecommendationGateway cbsRecommendationGateway,
      CbsRecommendationMapper cbsRecommendationMapper) {
    return new CbsRecommendationService(
        cbsRecommendationGateway,
        cbsRecommendationMapper,
        alertInFlightService);
  }

  @Bean
  @Conditional(OnAlertProcessorCondition.class)
  @ConditionalOnProperty(
      value = "silenteight.scb-bridge.cbs.attach-qco-fields-to-recom",
      havingValue = "false",
      matchIfMissing = true)
  RecomFunctionExecutorService standardRecomFunctionExecutorService(
      @Qualifier("externalDataSource") DataSource dataSource) {
    var jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.setQueryTimeout(scbBridgeConfigProperties.getQueryTimeout());
    return new StandardRecomFunctionExecutorService(
        cbsConfigProperties.getRecomFunctionName(),
        jdbcTemplate,
        cbsConfigProperties.getSourceApplicationValues());
  }

  @Bean
  @Conditional(OnAlertProcessorCondition.class)
  @ConditionalOnProperty(
      value = "silenteight.scb-bridge.cbs.attach-qco-fields-to-recom",
      havingValue = "true")
  RecomFunctionExecutorService qcoRecomFunctionExecutorService(
      @Qualifier("externalDataSource") DataSource dataSource) {
    var jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.setQueryTimeout(scbBridgeConfigProperties.getQueryTimeout());
    return new QcoRecomFunctionExecutorService(
        cbsConfigProperties.getRecomWithQcoFunctionName(),
        jdbcTemplate,
        cbsConfigProperties.getSourceApplicationValues());
  }

  @Bean
  @Conditional(OnAlertProcessorCondition.class)
  CbsRecommendationGateway cbsRecommendationGateway(
      RecomFunctionExecutorService recomFunctionExecutorService,
      ScbRecommendationService recommendationService) {
    var gateway = new CbsRecommendationGateway(recomFunctionExecutorService, recommendationService);
    gateway.setEventPublisher(eventPublisher);
    return gateway;
  }
}
