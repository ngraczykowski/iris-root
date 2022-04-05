package com.silenteight.scb.outputrecommendation.infrastructure;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsConfigProperties;
import com.silenteight.scb.outputrecommendation.adapter.outgoing.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@RequiredArgsConstructor
@EnableConfigurationProperties(CbsRecommendationProperties.class)
@Configuration
public class CbsRecommendationServiceConfiguration {

  private final ScbBridgeConfigProperties scbBridgeConfigProperties;
  private final CbsConfigProperties cbsConfigProperties;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  CbsRecommendationService cbsRecommendationService(
      CbsRecommendationGateway cbsRecommendationGateway,
      CbsRecommendationMapper cbsRecommendationMapper) {
    return new CbsRecommendationService(
        cbsRecommendationGateway,
        cbsRecommendationMapper);
  }

  @Bean
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
  CbsRecommendationGateway cbsRecommendationGateway(
      RecomFunctionExecutorService recomFunctionExecutorService) {
    CbsRecommendationGateway gateway = new CbsRecommendationGateway(recomFunctionExecutorService);
    gateway.setEventPublisher(eventPublisher);
    return gateway;
  }
}
