package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.hsbcbridgeclient.client.IngestGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.LearningGateway;
import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.AlertDataSource;
import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.DataGenerationService;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.time.LocalDate.of;

@Slf4j
@RequiredArgsConstructor
public class GenerateBatchUseCase {

  private final DataGenerationService dataGenerationService;

  private final IngestBatchGeneratorService ingestBatchGeneratorService;

  private final LearningBatchGeneratorService learningBatchGeneratorService;

  private final Integer batchSize;

  private final IngestGateway ingestGateway;

  private final LearningGateway learningGateway;

  private final DateTimeFormatter formatter;

  private final String batchPrefix;

  @SneakyThrows
  @Scheduled(cron = "${test.hsbc-bridge-client.cron}")
  // If you prefer to generate at startup, replace the line above with the line below:
  //  @EventListener(ApplicationReadyEvent.class)
  public void activate() {
    String batchId = generateBatchId();
    log.info("Batch generation started: batchId={}, batchSize={}", batchId, batchSize);

    List<AlertDataSource> alertDataSources = dataGenerationService.generateData(
        batchSize, batchId, of(2020, 1, 1), of(2022, 1, 1));

    JsonNode ingest = ingestBatchGeneratorService.generateBatch(batchId, alertDataSources);
    ingestGateway.send(ingest).block();

    JsonNode learning = learningBatchGeneratorService.generateBatch(alertDataSources);
    learningGateway.send(learning, batchId).block();

    log.info("Batch processed: batchId={}", batchId);
  }

  String generateBatchId() {
    String timestamp = LocalDate.now().format(formatter);
    String random = valueOf(new Random().nextInt(1000));
    return join("_", batchPrefix, random, timestamp);
  }
}
