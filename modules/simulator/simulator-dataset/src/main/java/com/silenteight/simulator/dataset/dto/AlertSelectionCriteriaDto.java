package com.silenteight.simulator.dataset.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder
public class AlertSelectionCriteriaDto {

  @NonNull
  RangeQueryDto alertGenerationDate;

  @NonNull
  List<String> countries;

  public OffsetDateTime getRangeFrom() {
    return alertGenerationDate.getFrom();
  }

  public OffsetDateTime getRangeTo() {
    return alertGenerationDate.getTo();
  }
}
