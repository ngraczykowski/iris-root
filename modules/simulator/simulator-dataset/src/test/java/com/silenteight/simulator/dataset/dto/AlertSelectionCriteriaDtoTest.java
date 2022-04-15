package com.silenteight.simulator.dataset.dto;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.*;

class AlertSelectionCriteriaDtoTest {

  private AlertSelectionCriteriaDto createDto(int hour) {
    return AlertSelectionCriteriaDto.builder()
        .alertGenerationDate(RangeQueryDto.builder()
            .from(OffsetDateTime.of(2019, 1, 1, 0, 0, 0, 0, UTC))
            .to(OffsetDateTime.of(2019, 1, 10, hour, 0, 0, 0, UTC))
            .build())
        .build();
  }

  @Test
  void displayRangeShouldBeWithoutTime() {
    AlertSelectionCriteriaDto dto = createDto(10);

    assertEquals("2019-01-01", dto.getDisplayRangeFrom());
    assertEquals("2019-01-10", dto.getDisplayRangeTo());
  }

  @Test
  void ifDateIsMidnightdisplayRangeToShouldBePreviousDay() {
    AlertSelectionCriteriaDto dto = createDto(0);

    assertEquals("2019-01-01", dto.getDisplayRangeFrom());
    assertEquals("2019-01-09", dto.getDisplayRangeTo());
  }
}