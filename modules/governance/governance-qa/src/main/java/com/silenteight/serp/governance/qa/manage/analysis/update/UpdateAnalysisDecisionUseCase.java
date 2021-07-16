package com.silenteight.serp.governance.qa.manage.analysis.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.domain.DecisionService;
import com.silenteight.serp.governance.qa.manage.domain.dto.CreateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;

@RequiredArgsConstructor
class UpdateAnalysisDecisionUseCase {

  @NonNull
  private final DecisionService decisionService;

  void activate(@NonNull UpdateDecisionRequest request) {
    decisionService.updateDecision(request);
    if (request.getState() == FAILED)
      decisionService.createDecision(createValidationDecisionRequest(request));
  }

  private static CreateDecisionRequest createValidationDecisionRequest(
      UpdateDecisionRequest request) {

    return CreateDecisionRequest.of(
        request.getDiscriminator(),
        NEW,
        VALIDATION,
        request.getCreatedBy(),
        request.getCreatedAt()
    );
  }
}
