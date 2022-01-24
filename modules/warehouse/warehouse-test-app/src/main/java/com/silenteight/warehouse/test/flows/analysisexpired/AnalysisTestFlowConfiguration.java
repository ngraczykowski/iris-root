package com.silenteight.warehouse.test.flows.analysisexpired;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.AnalysisExpiredClientGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
class AnalysisTestFlowConfiguration {

  @Value("${test.flows.analysis-expired.alert-count-in-analysis}")
  private Integer analysisCount;

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.flows.analysis-expired.enabled", havingValue = "true")
  AnalysisExpiredClient analysisExpiredClient(
      AnalysisExpiredClientGateway analysisExpiredClientGateway,
      JdbcTemplate jdbcTemplate) {

    log.info("AnalysisExpiredClient created");
    return new AnalysisExpiredClient(
        analysisExpiredClientGateway, new MessageGenerator(jdbcTemplate), analysisCount);
  }
}
