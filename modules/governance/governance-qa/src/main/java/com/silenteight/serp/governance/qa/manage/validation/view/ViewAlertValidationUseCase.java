package com.silenteight.serp.governance.qa.manage.validation.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;

@RequiredArgsConstructor
class ViewAlertValidationUseCase {

  @NonNull
  private final DecisionService decisionService;

  void activate(@NonNull ViewDecisionCommand command) {
    decisionService.view(command.getDiscriminator(), command.getLevel());
  }
}
