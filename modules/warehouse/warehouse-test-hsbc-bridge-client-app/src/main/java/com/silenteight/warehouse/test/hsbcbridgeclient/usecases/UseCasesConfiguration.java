package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import com.silenteight.warehouse.test.hsbcbridgeclient.client.IngestGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.LearningGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.RecommendGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.DataGenerationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Random;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.toList;

@Configuration
@EnableScheduling
public class UseCasesConfiguration {

  public static final String BATCH_DATETIME_FORMAT = "yyyyMMdd";

  @Value("${test.hsbc-bridge-client.datagen.alert-templates}")
  private List<String> alertTemplates;

  @Value("${test.hsbc-bridge-client.batch-size}")
  private Integer batchSize;

  @Value("${test.hsbc-bridge-client.batch-prefix}")
  private String batchPrefix;

  @Bean
  TemplateService templateService() {
    return new TemplateService(new ObjectMapper());
  }

  @Bean
  BatchGeneratorService learningBatchGeneratorService(
      TemplateService templateService, DataGenerationService dataGenerationService,
      ResourceLoader resourceLoader) {

    return new BatchGeneratorService(
        new ObjectMapper(),
        alertTemplates.stream()
            .map(resourceLoader::getResource)
            .collect(toList()),
        templateService,
        dataGenerationService,
        ofPattern(BATCH_DATETIME_FORMAT),
        batchPrefix,
        batchSize,
        new Random());
  }

  @Bean
  GenerateFullFlowUseCase generateIngestUseCase(
      BatchGeneratorService batchGeneratorService,
      IngestGateway ingestGateway,
      RecommendGateway recommendGateway,
      LearningGateway learningGateway) {

    return new GenerateFullFlowUseCase(
        batchGeneratorService,
        ingestGateway,
        recommendGateway,
        learningGateway);
  }
}
