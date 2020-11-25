package com.silenteight.serp.governance.autoactivation;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.commons.Result;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupCreated;

import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
public class AutoActivationFacade {

  private final ActivateDecisionGroupForDefaultDecisionTreeUseCase
      activateDecisionGroupForDefaultDecisionTreeUseCase;

  @EventListener
  public Result activateDecisionGroupForDefaultDecisionTreeUseCase(DecisionGroupCreated event) {
    long decisionGroupId = event.getDecisionGroupId();
    return activateDecisionGroupForDefaultDecisionTreeUseCase.activate(decisionGroupId);
  }

}
