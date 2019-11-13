package com.silenteight.sens.webapp.backend.application.decisiontree.assignments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentsUpdateDto {

  @NotNull
  private List<String> toAssign;

  @NonNull
  private List<String> toUnassign;

  @NotNull
  private List<String> toActivate;

  @NonNull
  private List<String> toDeactivate;
}
