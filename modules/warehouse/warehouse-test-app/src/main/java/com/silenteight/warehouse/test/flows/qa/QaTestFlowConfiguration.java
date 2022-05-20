package com.silenteight.warehouse.test.flows.qa;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.QaIndexClientGateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
class QaTestFlowConfiguration {

  @Bean
  @ConditionalOnProperty(value = "test.flows.qa.enabled", havingValue = "true")
  QaIndexClient qaIndexClient(
      QaIndexClientGateway qaIndexClientGateway, JdbcTemplate jdbcTemplate) {

    log.info("QaIndexClient created");
    AlertGenerator alertGenerator = new AlertGenerator(jdbcTemplate);

    return new QaIndexClient(qaIndexClientGateway, alertGenerator);
  }
}
