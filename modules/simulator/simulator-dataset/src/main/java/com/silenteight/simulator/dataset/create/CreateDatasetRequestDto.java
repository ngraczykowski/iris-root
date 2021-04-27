package com.silenteight.simulator.dataset.create;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;

import java.util.UUID;

@Value
@Builder
public class CreateDatasetRequestDto {

  @NonNull
  UUID id;

  @NonNull
  String datasetName;

  String description;

  @NonNull
  AlertSelectionCriteriaDto query;
}
