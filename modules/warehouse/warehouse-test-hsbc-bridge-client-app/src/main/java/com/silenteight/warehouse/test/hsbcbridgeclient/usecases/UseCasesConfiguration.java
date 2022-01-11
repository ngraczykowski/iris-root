package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import com.silenteight.warehouse.test.hsbcbridgeclient.client.IngestGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.LearningGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.DataGenerationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import static java.time.format.DateTimeFormatter.ofPattern;

@Configuration
@EnableScheduling
public class UseCasesConfiguration {

  public static final String BATCH_DATETIME_FORMAT = "yyyyMMdd";

  @Value("${test.hsbc-bridge-client.ingest.batch-template}")
  private Resource ingestBatchTemplate;

  @Value("${test.hsbc-bridge-client.ingest.alert-template}")
  private Resource ingestAlertTemplate;

  @Value("${test.hsbc-bridge-client.learning.alert-template}")
  private Resource learningAlertTemplate;

  @Value("${test.hsbc-bridge-client.batch-size}")
  private Integer batchSize;

  @Value("${test.hsbc-bridge-client.batch-prefix}")
  private String batchPrefix;

  @Bean
  TemplateService templateService() {
    return new TemplateService(new ObjectMapper());
  }

  @Bean
  IngestBatchGeneratorService ingestBatchGeneratorService(TemplateService templateService) {
    return new IngestBatchGeneratorService(
        new ObjectMapper(), ingestBatchTemplate, ingestAlertTemplate, templateService);
  }

  @Bean
  LearningBatchGeneratorService learningBatchGeneratorService(TemplateService templateService) {
    return new LearningBatchGeneratorService(
        new ObjectMapper(), learningAlertTemplate, templateService);
  }

  @Bean
  GenerateBatchUseCase generateIngestUseCase(
      DataGenerationService dataGenerationService,
      IngestBatchGeneratorService ingestBatchGeneratorService,
      LearningBatchGeneratorService learningBatchGeneratorService,
      IngestGateway ingestGateway,
      LearningGateway learningGateway) {

    return new GenerateBatchUseCase(
        dataGenerationService,
        ingestBatchGeneratorService,
        learningBatchGeneratorService,
        batchSize,
        ingestGateway,
        learningGateway,
        ofPattern(BATCH_DATETIME_FORMAT),
        batchPrefix);
  }
}
