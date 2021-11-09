package com.silenteight.warehouse.test.generator;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.PersonalInformationExpiredClientGateway;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.UUID.randomUUID;

@Slf4j
@Configuration
class IndexClientConfiguration {

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.generator.production-enabled", havingValue = "true")
  ProductionIndexClient productionIndexClient(
      ProductionIndexClientGateway productionIndexClientGateway, AlertGenerator alertGenerator) {

    log.info("ProductionIndexClient created");
    return new ProductionIndexClient(productionIndexClientGateway, alertGenerator);
  }

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.generator.simulation-enabled", havingValue = "true")
  SimulationIndexClient simulationIndexClient(
      SimulationIndexClientGateway simulationIndexClientGateway, AlertGenerator alertGenerator) {

    log.info("SimulationIndexClient created");
    return new SimulationIndexClient(
        simulationIndexClientGateway, alertGenerator, getAnalysisName());
  }

  @Bean
  @Autowired
  @ConditionalOnProperty(value = "test.generator.pii-expired-enabled", havingValue = "true")
  PersonalInformationDataExpiredClient personalInformationDataExpiredClient(
      PersonalInformationExpiredClientGateway personalInformationExpiredClientGateway,
      AlertGenerator alertGenerator) {

    log.info("PersonalInformationDataExpiredClient created");
    return new PersonalInformationDataExpiredClient(
        personalInformationExpiredClientGateway, alertGenerator);
  }

  @Bean
  @Autowired
  AlertGenerator alertGenerator(DataReader dataReader) {
    return new AlertGenerator(dataReader);
  }

  @Bean
  DataReader dataReader() {
    return new DataReader();
  }

  private String getAnalysisName() {
    return "analysis/" + randomUUID();
  }
}
