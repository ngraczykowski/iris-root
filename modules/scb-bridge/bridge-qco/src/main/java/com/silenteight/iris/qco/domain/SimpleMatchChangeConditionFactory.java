/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.model.ChangeCondition;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleMatchChangeConditionFactory implements ChangeConditionFactory {

  private final QcoConfigurationHolder configurationHolder;

  @Override
  public ChangeCondition createChangeCondition(String policyId, String stepId, String solution) {
    var changeCondition = new ChangeCondition(policyId, stepId, solution);
    boolean conditionExists = configurationHolder.getConfiguration().containsKey(changeCondition);
    if (!conditionExists) {
      log.debug("The Qco configuration was not found for "
              + "policyId={}, stepId={}, solution={} and hashCode={}",
          policyId, stepId, solution, changeCondition.hashCode());
      return ChangeCondition.NO_CONDITION_FULFILLED;
    }
    log.debug("The Qco configuration was found for policyId={}, stepId={}, solution={}",
        policyId, stepId, solution);
    return changeCondition;
  }
}
