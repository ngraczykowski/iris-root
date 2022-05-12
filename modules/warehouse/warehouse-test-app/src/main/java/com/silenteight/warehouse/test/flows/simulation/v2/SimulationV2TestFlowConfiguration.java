package com.silenteight.warehouse.test.flows.simulation.v2;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.SimulationV2IndexClientGateway;
import com.silenteight.warehouse.test.generator.DataReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
class SimulationV2TestFlowConfiguration {

  @Value("${test.flows.simulation.v2.alert-data-source}")
  private Resource alertDataSource;

  @Value("${test.flows.simulation.v2.match-data-source}")
  private Resource matchDataSource;

  @Value("${test.flows.simulation.v2.alert-count-in-analysis}")
  private Integer alertCount;

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.flows.simulation.v2.enabled", havingValue = "true")
  SimulationIndexClient simulationIndexClient(
      SimulationV2IndexClientGateway simulationIndexClientGatewayV2, JdbcTemplate jdbcTemplate) {

    log.info("SimulationIndexClient created");
    AlertGenerator alertGenerator = new AlertGenerator(
        new DataReader(alertDataSource),
        new DataReader(matchDataSource),
        jdbcTemplate);

    return new SimulationIndexClient(simulationIndexClientGatewayV2, alertGenerator, alertCount);
  }
}
