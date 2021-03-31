package com.silenteight.simulator.dataset.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.simulator.dataset.domain.DatasetState;

import java.time.OffsetDateTime;

@Value
@Builder
public class DatasetDto {

  @NonNull
  String datasetName;

  @NonNull
  String name;

  String description;

  @NonNull
  DatasetState state;

  long alertsCount;

  @NonNull
  AlertSelectionCriteriaDto query;

  @NonNull
  OffsetDateTime createdAt;

  @NonNull
  String createdBy;
}
