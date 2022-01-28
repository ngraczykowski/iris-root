package com.silenteight.payments.bridge.svb.migration;

import lombok.RequiredArgsConstructor;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class DecisionMapperConfiguration {

  private static final String DECISION_MAPPING_CSV =
      "classpath:analyst-decision-mapping/analyst-decision-mapping.csv";

  private final DecisionMapperFactory factory;
  private final ResourceLoader resourceLoader;

  @Bean
  public DecisionMapper decisionMapper() throws IOException, CsvValidationException {
    try (var inputStream = resourceLoader.getResource(DECISION_MAPPING_CSV).getInputStream()) {
      return factory.create(inputStream);
    }
  }
}
