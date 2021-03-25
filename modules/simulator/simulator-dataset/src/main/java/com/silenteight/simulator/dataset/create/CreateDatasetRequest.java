package com.silenteight.simulator.dataset.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
public class CreateDatasetRequest {

  @NonNull
  UUID datasetId;

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
