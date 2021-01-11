package com.silenteight.serp.governance.autoactivation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.activation.dto.ActivationRequest;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.decisiontree.DecisionTreeId;

import java.util.Optional;
import javax.annotation.Nonnull;

@Slf4j
@RequiredArgsConstructor
class ActivateDecisionGroupForDefaultDecisionTreeUseCase {

  private final DecisionTreeFacade decisionTreeFacade;
  private final ActivationService activationService;

  Result activate(long decisionGroupId) {
    return getDefaultDecisionTreeIdForNewGroups()
        .map(decisionTreeId -> activateDecisionGroup(decisionTreeId, decisionGroupId))
        .orElseGet(this::logDefaultDecisionTreeWasNotFound);
  }

  @Nonnull
  private Optional<DecisionTreeId> getDefaultDecisionTreeIdForNewGroups() {
    return decisionTreeFacade.findDefaultTreeForNewGroups();
  }

  @Nonnull
  private Result activateDecisionGroup(DecisionTreeId decisionTree, long decisionGroupId) {
    long decisionTreeId = decisionTree.getId();
    ActivationRequest request = ActivationRequest.of(decisionTreeId, decisionGroupId);
    activationService.activate(request);
    return Result.SUCCESS;
  }

  @Nonnull
  private Result logDefaultDecisionTreeWasNotFound() {
    if (log.isInfoEnabled()) {
      log.info(
          "No default Decision Tree for a new group found");
    }
    return Result.FAILURE;
  }
}
