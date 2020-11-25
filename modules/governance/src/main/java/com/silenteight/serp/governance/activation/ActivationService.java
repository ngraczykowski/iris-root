package com.silenteight.serp.governance.activation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.activation.dto.ActivationRequest;

import java.util.Collection;
import java.util.Optional;
import javax.transaction.Transactional;

import static com.silenteight.sep.base.common.logging.LogContextUtils.logObject;

@Slf4j
@RequiredArgsConstructor
public class ActivationService {

  private final ActivationRepository activationRepository;

  @Transactional
  public void activate(ActivationRequest activationRequest) {
    Collection<Long> decisionGroupsIds = activationRequest.getDecisionGroupIds();
    long treeId = activationRequest.getDecisionTreeId();

    decisionGroupsIds.forEach(groupId -> activate(treeId, groupId));
  }

  private void activate(long decisionTreeId, long decisionGroupId) {
    Optional<Activation> activationOpt = activationRepository
        .findByDecisionGroupId(decisionGroupId);

    activationOpt.ifPresentOrElse(
        activation -> activation.setDecisionTreeId(decisionTreeId),
        () -> createNewActivation(decisionTreeId, decisionGroupId)
    );
    logObject("decisionTree.id", decisionTreeId);
    logObject("decisionGroup.id", decisionGroupId);
    if (log.isDebugEnabled())
      log.debug(
          "Decision tree activated: decisionTreeId={}, decisionGroupId={}",
          decisionTreeId,
          decisionGroupId);
  }

  private void createNewActivation(long decisionTreeId, long decisionGroupId) {
    activationRepository.save(new Activation(decisionGroupId, decisionTreeId));
  }

  @Transactional
  public void deactivate(long decisionTreeId, long decisionGroupId) {
    activationRepository.deleteByDecisionTreeIdAndDecisionGroupId(decisionTreeId, decisionGroupId);
    logObject("decisionTree.id", decisionTreeId);
    logObject("decisionGroup.id", decisionGroupId);
    if (log.isDebugEnabled())
      log.debug(
          "Decision tree deactivated: decisionTreeId={}, decisionGroupId={}",
          decisionTreeId,
          decisionGroupId);
  }
}
