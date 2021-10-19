package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.opencsv.CSVReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Configuration
@RequiredArgsConstructor
class DecisionMapperConfiguration {

  private static final String DECISION_MAPPING_CSV =
      "classpath:analyst-decision-mapping/analyst-decision-mapping.csv";

  private final ResourceLoader resourceLoader;

  @Bean
  public DecisionMapper decisionMapper() {
    try (var csvReader = new CSVReader(new InputStreamReader(
        resourceLoader.getResource(DECISION_MAPPING_CSV).getInputStream()))) {
      Map<String, String> decisionMap = new HashMap<>();
      String defaultValue = null;

      String[] header = csvReader.readNext();
      String[] row;
      while ((row = csvReader.readNext()) != null) {
        if ("*".equals(row[0])) {
          defaultValue = row[1];
        } else {
          decisionMap.put(row[0], row[1]);
        }
      }

      if (defaultValue == null) {
        throw new IllegalArgumentException();
      }

      return new DecisionMapper(decisionMap, defaultValue);

    } catch (Exception exception) {
      return rethrow(exception);
    }
  }

  @RequiredArgsConstructor
  public static class DecisionMapper {
    private final Map<String, String> decisionMap;
    private final String defaultValue;

    public String map(String input) {
      return decisionMap.getOrDefault(input, defaultValue);
    }
  }

}
