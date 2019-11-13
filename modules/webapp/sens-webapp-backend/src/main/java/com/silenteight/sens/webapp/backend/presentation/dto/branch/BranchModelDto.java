package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class BranchModelDto {

  private final List<String> featureNames;
}
