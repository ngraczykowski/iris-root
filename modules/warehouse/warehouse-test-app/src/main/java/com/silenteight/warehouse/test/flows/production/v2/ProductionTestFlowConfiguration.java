package com.silenteight.warehouse.test.flows.production.v2;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;
import com.silenteight.warehouse.test.generator.DataReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
class ProductionTestFlowConfiguration {

  @Value("${test.flows.production.data-source}")
  private Resource productionResourceFile;

  @Value("${test.flows.production.alert-count}")
  private Integer alertCount;

  @Bean
  @ConditionalOnProperty(value = "test.flows.production.enabled", havingValue = "true")
  ProductionIndexClient productionIndexClient(
      ProductionIndexClientGateway productionIndexClientGateway) {

    log.info("ProductionIndexClient created");
    AlertGenerator alertGenerator = new AlertGenerator(new DataReader(productionResourceFile));

    return new ProductionIndexClient(productionIndexClientGateway, alertGenerator, alertCount);
  }
}
