package com.silenteight.sens.webapp.backend.rest.decisiontree.dto;

import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.domain.decisiontree.DecisionTreeView;
import com.silenteight.sens.webapp.backend.domain.decisiontree.Status;

import java.util.List;

@Data
public class DecisionTreeDto {

  private final long id;
  @NonNull
  private final String name;
  @NonNull
  private final StatusDto status;
  @NonNull
  private final List<String> activations;

  public DecisionTreeDto(DecisionTreeView view) {
    id = view.getId();
    name = view.getName();
    status = createStatus(view.getActivations());
    activations = view.getActivations();
  }

  private static StatusDto createStatus(List<String> activations) {
    return StatusDto
        .builder()
        .name(createStatusName(activations))
        .build();
  }

  private static String createStatusName(List<String> activations) {
    return activations.isEmpty() ? Status.INACTIVE.name() : Status.ACTIVE.name();
  }
}
