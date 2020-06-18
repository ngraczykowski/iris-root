package com.silenteight.sens.webapp.backend.reasoningbranch.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ReasoningBranchesPageDto {

  @NonNull
  private final List<ReasoningBranchDto> branches;
  private final long total;

  public int pageSize() {
    return branches.size();
  }
}
