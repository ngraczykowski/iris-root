package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class BranchResponseDto {

  private final long total;
  private final BranchModelDto branchModel;
  @NonNull
  private final List<BranchDto> branches;
}
