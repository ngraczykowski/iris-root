package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListBranchesRequestDto {

  @NonNull
  private List<Long> branchIds;
}
