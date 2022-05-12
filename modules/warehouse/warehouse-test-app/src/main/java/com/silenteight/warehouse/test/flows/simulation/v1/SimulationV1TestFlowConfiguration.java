package com.silenteight.warehouse.test.flows.simulation.v1;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.SimulationV1IndexClientGateway;
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
class SimulationV1TestFlowConfiguration {

  @Value("${test.flows.simulation.v1.data-source}")
  private Resource resourceFile;

  @Value("${test.flows.simulation.v1.alert-count-in-analysis}")
  private Integer alertCount;

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.flows.simulation.v1.enabled", havingValue = "true")
  SimulationIndexClient simulationIndexClient(
      SimulationV1IndexClientGateway simulationV1IndexClientGateway, JdbcTemplate jdbcTemplate) {

    log.info("SimulationIndexClient created");
    AlertGenerator alertGenerator = new AlertGenerator(new DataReader(resourceFile), jdbcTemplate);

    return new SimulationIndexClient(simulationV1IndexClientGateway, alertGenerator, alertCount);
  }
}
