package com.silenteight.warehouse.test.generator;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.UUID.randomUUID;

@Slf4j
@Configuration
class IndexClientConfiguration {

  @Bean
  @ConditionalOnProperty(value = "test.generator.production-enabled", havingValue = "true")
  ProductionIndexClient productionIndexClient(
      ProductionIndexClientGateway productionIndexClientGateway) {

    log.info("ProductionIndexClient created");
    return new ProductionIndexClient(productionIndexClientGateway, new AlertGenerator());
  }

  @Bean
  @ConditionalOnProperty(value = "test.generator.simulation-enabled", havingValue = "true")
  SimulationIndexClient simulationIndexClient(
      SimulationIndexClientGateway simulationIndexClientGateway) {

    log.info("SimulationIndexClient created");
    return new SimulationIndexClient(
        simulationIndexClientGateway, new AlertGenerator(), getAnalysisName());
  }

  @Bean
  AlertGenerator alertGenerator() {
    return new AlertGenerator();
  }

  private String getAnalysisName() {
    return "analysis/" + randomUUID().toString();
  }
}
