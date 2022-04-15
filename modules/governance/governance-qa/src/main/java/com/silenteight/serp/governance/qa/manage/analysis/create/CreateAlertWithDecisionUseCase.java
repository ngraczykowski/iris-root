package com.silenteight.serp.governance.qa.manage.analysis.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;

@Slf4j
@RequiredArgsConstructor
public class CreateAlertWithDecisionUseCase {

  @NonNull
  private final DecisionService decisionService;

  public void activate(CreateDecisionRequest request) {
    log.info("CreateDecisionRequest request received, request={}", request);
    decisionService.addAlert(request.getAlertName());
    decisionService.createDecision(request);
    log.debug("CreateDecisionRequest request processed.");
  }
}
