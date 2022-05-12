package com.silenteight.warehouse.test.hsbcbridgeclient.datageneration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataGeneratorConfiguration {

  @Bean
  DataGenerationService dataGenerationService() {
    return new DataGenerationService();
  }
}
