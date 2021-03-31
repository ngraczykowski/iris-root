package com.silenteight.simulator.dataset.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class AlertSelectionCriteriaDto {

  @NonNull
  RangeQueryDto alertGenerationDate;

  public OffsetDateTime getRangeFrom() {
    return alertGenerationDate.getFrom();
  }

  public OffsetDateTime getRangeTo() {
    return alertGenerationDate.getTo();
  }
}
