package com.silenteight.warehouse.test.flows.simulation;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;
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
class SimulationTestFlowConfiguration {

  @Value("${test.flows.simulation.data-source}")
  private Resource resourceFile;

  @Value("${test.flows.simulation.alert-count-in-analysis}")
  private Integer alertCount;

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.flows.simulation.enabled", havingValue = "true")
  SimulationIndexClient simulationIndexClient(
      SimulationIndexClientGateway simulationIndexClientGateway, JdbcTemplate jdbcTemplate) {

    log.info("SimulationIndexClient created");
    AlertGenerator alertGenerator = new AlertGenerator(new DataReader(resourceFile), jdbcTemplate);

    return new SimulationIndexClient(simulationIndexClientGateway, alertGenerator, alertCount);
  }
}
