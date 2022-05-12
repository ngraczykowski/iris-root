package com.silenteight.warehouse.test.hsbcbridgeclient.datageneration;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.randomUUID;

@Slf4j
public class DataGenerationService {

  private final Random random = new Random();

  public List<AlertDataSource> generateData(
      Integer batchSize, String batchId) {

    List<AlertDataSource> data = IntStream.range(0, batchSize)
        .mapToObj(i -> generateDataSingleAlert(i, batchId))
        .collect(Collectors.toList());

    log.info("DataGenerationService: data generated, size={}", batchSize);
    return data;
  }

  AlertDataSource generateDataSingleAlert(
      Integer alertNumber, String batchId) {
    String alertId = String.join("_", "testalert", batchId, alertNumber.toString());
    String flagKey = randomUUID().toString();
    String alertDate = LocalDateTime.now().format(ofPattern("dd-LLL-yy")).toUpperCase();
    String caseId = randomUUID().toString();
    String currentState = getRandomValue(
        "True Match Exit Completed", "False Positive", "False Positive", "Level 1 Review");

    return AlertDataSource.builder()
        .alertId(alertId)
        .flagKey(flagKey)
        .alertDate(alertDate)
        .caseId(caseId)
        .currentState(currentState)
        .build();
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }
}
