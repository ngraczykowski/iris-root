package com.silenteight.payments.bridge.agents.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.validation.Valid;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Configuration
@Slf4j
@EnableConfigurationProperties(SpecificTerms2Properties.class)
class SpecificTerms2Configuration {

  private static final String RESPONSE_YES_PTP = "YES_PTP";
  private static final String RESPONSE_YES = "YES";

  private final SpecificTerms2TermsProvider specificTerms2TermsProvider;

  @Valid private final SpecificTerms2Properties properties;

  @Bean
  @Profile("mockaws")
  SpecificTerms2Agent specificTerms2DefaultAgent() {
    return new SpecificTerms2Agent(buildAgentMappings(getDefaultMappings()));
  }

  @Bean
  @Profile("!mockaws")
  SpecificTerms2Agent specificTerms2Agent() {
    return new SpecificTerms2Agent(buildAgentMappings(getAwsMappings()));
  }

  private static List<SpecificTerms2Agent.Mapping> buildAgentMappings(List<Mapping> mappings) {
    return mappings
        .stream()
        .map(toSpecificTermsMapping())
        .collect(toList());
  }

  @Nonnull
  private static Function<Mapping, SpecificTerms2Agent.Mapping> toSpecificTermsMapping() {
    return mapping -> new SpecificTerms2Agent
        .Mapping(mapping.getPatterns(), mapping.getResponse());
  }

  private List<Mapping> getDefaultMappings() {
    return List.of(
        new Mapping(
            properties.getSpecificTerms().stream()
                .map(String::toUpperCase)
                .collect(toList()),
            RESPONSE_YES_PTP),
        new Mapping(properties.getRegularTerms(), RESPONSE_YES));
  }

  private List<Mapping> getAwsMappings() {
    try {
      return List.of(
          new Mapping(
              specificTerms2TermsProvider.getTermsFromProceededCsvFile(
                  properties.getSpecificTermsKey(), properties.getBucket()),
              RESPONSE_YES_PTP),
          new Mapping(
              specificTerms2TermsProvider.getTermsFromProceededCsvFile(
                  properties.getRegularTermsKey(), properties.getBucket()),
              RESPONSE_YES));
    } catch (CsvValidationException | NoSuchKeyException | IOException e) {
      log.error(
          "There was a problem when receiving or proceeding s3 object. "
              + "Using default mapping - Message: {}, Reason: {}",
          e.getMessage(),
          e.getCause());
      return getDefaultMappings();
    }
  }

  @Data
  @AllArgsConstructor
  static class Mapping {

    List<String> patterns;
    String response;
  }
}
