package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.AlertDataSource;
import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.DataGenerationService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class BatchGeneratorService {

  private final ObjectMapper objectMapper;

  private final List<Resource> alertTemplates;

  private final TemplateService templateService;

  private final DataGenerationService dataGenerationService;

  private final DateTimeFormatter formatter;

  private final String batchPrefix;

  private final Integer batchSize;

  private final Random random;

  public Batch generateBatch() {
    String batchId = generateBatchId();
    log.info("Batch generation started: batchId={}, batchSize={}", batchId, batchSize);

    List<AlertDataSource> alertDataSources = dataGenerationService.generateData(
        batchSize, batchId);

    return Batch.builder()
        .batchId(batchId)
        .status("NEW")
        .payload(generatePayload(alertDataSources))
        .build();
  }

  @SneakyThrows
  private byte[] generatePayload(List<AlertDataSource> alertsDataSource) {
    List<ObjectNode> alerts = alertsDataSource.stream()
        .map(alertDataSource -> objectMapper.convertValue(alertDataSource, Map.class))
        .map(alertDataMap -> templateService.template(getRandomAlertTemplate(), alertDataMap))
        .collect(toList());

    ArrayNode jsonNodes = objectMapper.createArrayNode()
        .addAll(alerts);

    return objectMapper.writeValueAsBytes(jsonNodes);
  }

  private String generateBatchId() {
    String timestamp = LocalDate.now().format(formatter);
    String randomValue = valueOf(random.nextInt(1000));
    return join("_", batchPrefix, randomValue, timestamp);
  }

  private Resource getRandomAlertTemplate() {
    int i = random.nextInt(alertTemplates.size());
    return alertTemplates.get(i);
  }
}
