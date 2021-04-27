package com.silenteight.simulator.dataset.create.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.dataset.dto.AlertSelectionCriteriaDto;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatasetRequestDto {

  @NonNull
  private UUID id;
  @NonNull
  private String datasetName;
  private String description;
  @NonNull
  private AlertSelectionCriteriaDto query;
}
