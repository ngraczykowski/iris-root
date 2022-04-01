package com.silenteight.qco.infrastructure.parser;

import lombok.RequiredArgsConstructor;

import com.silenteight.qco.domain.model.QcoPolicyStepSolutionOverrideConfiguration;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import static com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.TRIM_SPACES;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SolutionConfigurationFileProperties.class)
class FileParserConfiguration {

  private final ResourceLoader resourceLoader;
  private final SolutionConfigurationFileProperties properties;

  @Bean
  CsvSolutionConfigurationProvider solutionConfigurationCsvFileParser(
      CsvStreamParser<QcoPolicyStepSolutionOverrideConfiguration> csvStreamParser) {
    var resource = resourceLoader.getResource(properties.location());
    return new CsvSolutionConfigurationProvider(csvStreamParser, resource);
  }

  @Bean
  CsvStreamParser<QcoPolicyStepSolutionOverrideConfiguration> csvStreamParser(CsvMapper csvMapper) {
    return new CsvStreamParser<>(csvMapper, properties.separator());
  }

  @Bean
  CsvMapper csvMapper() {
    CsvMapper mapper = new CsvMapper();
    mapper.enable(TRIM_SPACES);
    return mapper;
  }
}
