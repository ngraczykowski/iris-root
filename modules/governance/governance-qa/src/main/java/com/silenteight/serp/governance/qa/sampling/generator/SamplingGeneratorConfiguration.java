package com.silenteight.serp.governance.qa.sampling.generator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SamplingGeneratorConfiguration {

  @Bean
  AlertsGeneratorService alertsGeneratorService() {
    return new AlertsGeneratorService(List.of());
  }
}
