package com.silenteight.serp.governance.qa.validation.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.domain.DecisionService;

@RequiredArgsConstructor
class ViewAlertValidationUseCase {

  @NonNull
  DecisionService decisionService;

  void activate(@NonNull ViewDecisionCommand command) {
    decisionService.view(command.getAlertName(), command.getLevel());
  }
}
