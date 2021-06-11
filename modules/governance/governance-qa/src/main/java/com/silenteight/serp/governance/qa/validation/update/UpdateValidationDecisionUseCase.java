package com.silenteight.serp.governance.qa.validation.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.domain.DecisionService;
import com.silenteight.serp.governance.qa.domain.dto.UpdateDecisionRequest;

@RequiredArgsConstructor
class UpdateValidationDecisionUseCase {

  @NonNull
  DecisionService decisionService;

  void activate(@NonNull UpdateDecisionRequest request) {
    decisionService.updateDecision(request);
  }
}

