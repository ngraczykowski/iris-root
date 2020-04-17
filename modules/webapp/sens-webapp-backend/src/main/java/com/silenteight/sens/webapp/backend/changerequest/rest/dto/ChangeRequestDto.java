package com.silenteight.sens.webapp.backend.changerequest.rest.dto;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRequestDto {

  @NonNull
  private Long id;
  @NonNull
  private String createdBy;
  @NonNull
  private OffsetDateTime createdAt;
  private Integer affectedBranchesCount;
  @NonNull
  private BranchAiSolutionDto branchAiSolution;
  @NonNull
  private BranchStatusDto branchStatus;
  @NonNull
  private String comment;
}
