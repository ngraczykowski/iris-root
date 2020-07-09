package com.silenteight.sens.webapp.backend.reasoningbranch.list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListReasoningBranchesRequestDto {

  @Nullable
  private String aiSolution;
  @Nullable
  private Boolean active;
  @Min(0)
  private int pageIndex;
  @Min(1)
  private int pageSize;
}
