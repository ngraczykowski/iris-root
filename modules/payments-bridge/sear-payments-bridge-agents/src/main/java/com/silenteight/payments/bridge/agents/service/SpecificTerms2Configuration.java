package com.silenteight.payments.bridge.agents.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SpecificTerms2Properties.class)
class SpecificTerms2Configuration {

  private static final String RESPONSE_YES_PTP = "YES_PTP";
  private static final String RESPONSE_YES = "YES";

  private final SpecificTerms2AwsFileProvider specificTerms2AwsFileProvider;

  @Valid
  private final SpecificTerms2Properties properties;

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

  private List<SpecificTerms2Agent.Mapping> buildAgentMappings(List<Mapping> mappings) {
    return mappings
        .stream()
        .map(toSpecificTermsMapping())
        .collect(Collectors.toList());
  }

  @Nonnull
  private Function<Mapping, SpecificTerms2Agent.Mapping> toSpecificTermsMapping() {
    return mapping -> new SpecificTerms2Agent.Mapping(
        mapping.getPatterns(), mapping.getResponse());
  }

  private List<Mapping> getDefaultMappings() {
    return List.of(
        new Mapping(
            properties
                .getSpecificTerms()
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList()),
            RESPONSE_YES_PTP),
        new Mapping(properties.getRegularTerms(), RESPONSE_YES));
  }

  private List<Mapping> getAwsMappings() {
    return List.of(
        new Mapping(
            specificTerms2AwsFileProvider.proceedCsvFile(
                properties.getSpecificTermsKey(),
                properties.getBucket(),
                properties.getRegion()), RESPONSE_YES_PTP),
        new Mapping(
            specificTerms2AwsFileProvider.proceedCsvFile(
                properties.getRegularTermsKey(),
                properties.getBucket(),
                properties.getRegion()), RESPONSE_YES)
    );
  }

  @Data
  @AllArgsConstructor
  static class Mapping {

    List<String> patterns;
    String response;
  }
}

