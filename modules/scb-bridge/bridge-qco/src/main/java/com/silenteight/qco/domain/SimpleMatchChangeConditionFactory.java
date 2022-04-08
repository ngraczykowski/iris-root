package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.ChangeCondition;

import org.springframework.stereotype.Component;

import static com.silenteight.qco.domain.model.ChangeCondition.NO_CONDITION_FULFILLED;

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
      log.warn("The Qco configuration was not found for policyId={}, stepId={}, solution={}",
          policyId, stepId, solution);
      return NO_CONDITION_FULFILLED;
    }
    log.debug("The Qco configuration was found for policyId={}, stepId={}, solution={}",
        policyId, stepId, solution);
    return changeCondition;
  }
}
