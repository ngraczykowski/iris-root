package com.silenteight.customerbridge.cbs.gateway;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.beans.factory.annotation.Qualifier;
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
  private final CbsRecommendationProperties cbsRecommendationProperties;
  private final CbsConfigProperties cbsConfigProperties;
  private final ApplicationEventPublisher eventPublisher;

  @Bean
  CbsRecommendationService cbsRecommendationService(
      CbsRecommendationGateway cbsRecommendationGateway) {
    return new CbsRecommendationService(
        cbsRecommendationGateway, cbsRecommendationProperties.getRecommendationValues());
  }

  @Bean
  CbsRecommendationGateway cbsRecommendationGateway(
      @Qualifier("externalDataSource") DataSource dataSource) {
    var jdbcTemplate = new JdbcTemplate(dataSource);
    jdbcTemplate.setQueryTimeout(scbBridgeConfigProperties.getQueryTimeout());

    CbsRecommendationGateway gateway = new CbsRecommendationGateway(
        cbsConfigProperties.getRecomFunctionName(),
        jdbcTemplate,
        cbsConfigProperties.getSourceApplicationValues());

    gateway.setEventPublisher(eventPublisher);
    return gateway;
  }
}
