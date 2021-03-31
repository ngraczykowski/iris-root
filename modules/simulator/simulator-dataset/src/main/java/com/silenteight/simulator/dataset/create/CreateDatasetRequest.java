package com.silenteight.simulator.dataset.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;

import java.time.OffsetDateTime;

@Value
@Builder
public class CreateDatasetRequest {

  @NonNull
  String name;

  String description;

  @NonNull
  AlertSelectionCriteriaDto query;

  public OffsetDateTime getRangeFrom() {
    return query.getRangeFrom();
  }

  public OffsetDateTime getRangeTo() {
    return query.getRangeTo();
  }
}
