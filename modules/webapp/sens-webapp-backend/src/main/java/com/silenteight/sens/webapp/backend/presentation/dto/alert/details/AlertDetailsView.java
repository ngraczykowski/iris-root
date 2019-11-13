package com.silenteight.sens.webapp.backend.presentation.dto.alert.details;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.presentation.dto.alert.PartyDto;
import com.silenteight.sens.webapp.backend.presentation.dto.alert.SolutionDto;

import java.util.List;

@Data
@Builder
public class AlertDetailsView {

  @NonNull
  private final Long id;
  @NonNull
  private final String externalId;
  @NonNull
  private final PartyDto party;
  private final SolutionDto analystSolution;
  private final SolutionDto aiSolution;
  @NonNull
  private final List<String> matchFieldNames;
  @NonNull
  private final List<AlertBranchView> branchInfos;
}
