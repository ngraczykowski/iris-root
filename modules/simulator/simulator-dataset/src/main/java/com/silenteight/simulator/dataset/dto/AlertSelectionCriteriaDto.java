package com.silenteight.simulator.dataset.dto;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.List.of;

@Value
@Builder
public class AlertSelectionCriteriaDto {

  @NonNull
  RangeQueryDto alertGenerationDate;

  @Default
  List<String> countries = of();

  public OffsetDateTime getRangeFrom() {
    return alertGenerationDate.getFrom();
  }

  public OffsetDateTime getRangeTo() {
    return alertGenerationDate.getTo();
  }

  public String getDisplayRangeFrom() {
    return alertGenerationDate.getFrom()
        .format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public String getDisplayRangeTo() {
    return alertGenerationDate.getTo()
        .minus(1, ChronoUnit.SECONDS)
        .format(DateTimeFormatter.ISO_LOCAL_DATE);
  }
}
