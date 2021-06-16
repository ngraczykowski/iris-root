package com.silenteight.simulator.dataset.dto;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
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
}
