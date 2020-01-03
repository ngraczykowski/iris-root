package com.silenteight.sens.webapp.backend.rest.decisiontree.dto;

import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.domain.decisiontree.DecisionTreeView;
import com.silenteight.sens.webapp.backend.rest.model.dto.ModelDto;

@Data
public class DecisionTreeDto {

  private final long id;
  @NonNull
  private final String name;
  @NonNull
  private final StatusDto status;
  @NonNull
  private final ModelDto model;

  public DecisionTreeDto(DecisionTreeView view) {
    id = view.getId();
    name = view.getName();
    status = createStatus(view.getStatus());
    model = createModel(view.getModelName());
  }

  private static StatusDto createStatus(String name) {
    return StatusDto
        .builder()
        .name(name)
        .build();
  }

  private static ModelDto createModel(String name) {
    return ModelDto
        .builder()
        // will be removed in next MR
        .id(1L)
        .name(name)
        .build();
  }
}
