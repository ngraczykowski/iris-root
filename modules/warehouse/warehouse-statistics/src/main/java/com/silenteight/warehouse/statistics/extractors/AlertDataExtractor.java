package com.silenteight.warehouse.statistics.extractors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.alert.AlertRepository;
import com.silenteight.warehouse.indexer.alert.dto.AlertDto;

import com.google.common.collect.Range;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * Provides alerts which can be used to do statistics calculations.
 */
@RequiredArgsConstructor
public final class AlertDataExtractor implements DataExtractor<AlertDto> {

  @NonNull
  private final AlertRepository alertRepository;

  private static OffsetDateTime getOffsetDateTime(LocalDate localDate, LocalTime localTime) {
    return OffsetDateTime.of(localDate, localTime, ZoneOffset.UTC);
  }

  @Override
  public LocalDate getEarliestDate() {
    return alertRepository.getEarliestAlertLocaDate();
  }

  @Override
  public List<AlertDto> getData(Range<LocalDate> range) {
    return alertRepository.fetchAlerts(
        getOffsetDateTime(range.lowerEndpoint(), LocalTime.MIN),
        getOffsetDateTime(range.upperEndpoint(), LocalTime.MAX));
  }
}
