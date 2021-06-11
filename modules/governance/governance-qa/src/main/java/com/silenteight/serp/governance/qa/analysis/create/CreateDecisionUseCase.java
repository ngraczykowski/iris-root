package com.silenteight.serp.governance.qa.analysis.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.domain.DecisionService;
import com.silenteight.serp.governance.qa.domain.dto.CreateDecisionRequest;

@RequiredArgsConstructor
class CreateDecisionUseCase {

  @NonNull
  private final DecisionService decisionService;

  void activate(CreateDecisionRequest request) {
    decisionService.createDecision(request);
  }
}
