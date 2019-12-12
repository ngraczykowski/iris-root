package com.silenteight.sens.webapp.backend.presentation.dto.assignment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Data
public class AssignmentsDto {

  @NonNull
  private final List<AvailableBatchType> available;

  @NonNull
  private final List<String> assigned;

  @NonNull
  private final List<String> activated;

  @Data
  @AllArgsConstructor
  public static class AvailableBatchType implements Comparable<AvailableBatchType> {

    @NonNull
    private final String batchType;

    private final boolean canActivate;

    @Nullable
    private final Long decisionTreeId;

    @Nullable
    private final String decisionTreeName;

    @Override
    public int compareTo(@NotNull AvailableBatchType o) {
      return batchType.compareTo(o.batchType);
    }
  }
}
