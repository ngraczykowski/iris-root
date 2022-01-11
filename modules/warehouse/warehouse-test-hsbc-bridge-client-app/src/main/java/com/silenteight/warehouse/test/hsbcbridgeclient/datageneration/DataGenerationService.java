package com.silenteight.warehouse.test.hsbcbridgeclient.datageneration;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.randomUUID;

@Slf4j
public class DataGenerationService {

  private static final DateTimeFormatter ISO_FORMAT = ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSX");

  public List<AlertDataSource> generateData(
      Integer batchSize, String batchId, LocalDate from, LocalDate to) {

    List<AlertDataSource> data = IntStream.range(0, batchSize)
        .mapToObj(i -> generateDataSingleAlert(i, batchId, from, to))
        .collect(Collectors.toList());

    log.info("DataGenerationService: data generated, size={}", batchSize);
    return data;
  }

  AlertDataSource generateDataSingleAlert(
      Integer alertNumber, String batchId, LocalDate from, LocalDate to) {

    LocalDate recommendationDate = between(from, to);
    String recommendationDateFormatted = recommendationDate.atStartOfDay()
        .atOffset(UTC)
        .format(ISO_FORMAT);
    String recommendationYear = valueOf(recommendationDate.getYear());
    String recommendationMonth = valueOf(recommendationDate.getMonthValue());
    String recommendationDay = valueOf(recommendationDate.getDayOfMonth());
    String alertId = String.join("_", "testalert", batchId, alertNumber.toString());
    String flagKey = randomUUID().toString();

    return AlertDataSource.builder()
        .alertId(alertId)
        .flagKey(flagKey)
        .recommendationDate(recommendationDateFormatted)
        .recommendationYear(recommendationYear)
        .recommendationMonth(recommendationMonth)
        .recommendationDay(recommendationDay)
        .build();
  }

  public static LocalDate between(LocalDate startInclusive, LocalDate endExclusive) {
    long startEpochDay = startInclusive.toEpochDay();
    long endEpochDay = endExclusive.toEpochDay();
    long randomDay = ThreadLocalRandom
        .current()
        .nextLong(startEpochDay, endEpochDay);

    return LocalDate.ofEpochDay(randomDay);
  }
}
