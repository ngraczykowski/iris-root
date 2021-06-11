package com.silenteight.serp.governance.qa.analysis.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.domain.DecisionService;

@RequiredArgsConstructor
class ViewAlertAnalysisUseCase {

  @NonNull
  private final DecisionService decisionService;

  void activate(@NonNull ViewDecisionCommand command) {
    decisionService.view(command.getAlertName(), command.getLevel());
  }
}
