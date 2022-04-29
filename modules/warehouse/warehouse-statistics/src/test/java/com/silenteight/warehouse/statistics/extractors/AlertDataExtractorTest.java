package com.silenteight.warehouse.statistics.extractors;

import com.silenteight.warehouse.indexer.alert.AlertRepository;

import com.google.common.collect.Range;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertDataExtractorTest {

  @Mock
  private AlertRepository repository;

  private AlertDataExtractor extractor;

  @BeforeEach
  void setUp() {
    extractor = new AlertDataExtractor(repository);
  }

  @Test
  void getAlertFromRange_entireRangeFromMinToMaxIsTaken() {

    // Given & When
    extractor.getData(Range.closed(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 2)));

    // Then
    verify(repository).fetchAlerts(
        OffsetDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
        OffsetDateTime.of(2022, 1, 2, 23, 59, 59, 999999999, ZoneOffset.UTC));
  }
}