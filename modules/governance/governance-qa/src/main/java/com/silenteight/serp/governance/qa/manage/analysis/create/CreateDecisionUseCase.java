package com.silenteight.serp.governance.qa.manage.analysis.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;

@RequiredArgsConstructor
public class CreateDecisionUseCase {

  @NonNull
  private final DecisionService decisionService;

  public void activate(CreateDecisionRequest request) {
    decisionService.createDecision(request);
  }
}
