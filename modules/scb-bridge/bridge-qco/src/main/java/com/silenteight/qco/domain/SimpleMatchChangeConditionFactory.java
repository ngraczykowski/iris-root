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
    //Temporary logs
    log.debug("Input params policyId: {}, stepId:{}, solution: {}", policyId, stepId, solution);
    log.debug("data of changeCondition: {}, hashCode:{} ",
        changeCondition, changeCondition.hashCode());
    log.debug(
        "Number of defined steps in qco configuration: {}",
        configurationHolder.getConfiguration().size());
    configurationHolder.getConfiguration()
        .forEach((key, param) ->
            log.debug("key: {}, param: {}, hashCode of key: {}",
                key.toString(), param.toString(), key.hashCode()));

    //end of temporary logs
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
