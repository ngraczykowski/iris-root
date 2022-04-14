package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.silenteight.qco.domain.model.ResolverAction.NOT_CHANGE;
import static com.silenteight.qco.domain.model.ResolverAction.OVERRIDE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchProcessor {

  private final ChangeConditionFactory changeConditionFactory;
  private final QcoCounter counter;
  private final MatchResolver resolver;

  public MatchSolution processMatch(QcoRecommendationMatch match) {
    log.debug("The processing of {} was started.", match.toString());
    var changeCondition = changeConditionFactory
        .createChangeCondition(match.policyId(), match.stepId(), match.solution());
    var resolverAction = determineResolverAction(changeCondition);
    log.debug("QCO module determined ResolverAction={} for matchName={}",
        resolverAction.name(), match.matchName());
    var resolverCommand = new ResolverCommand(match, changeCondition, resolverAction);
    log.debug("QCO module is calling resolver for matchName={}", match.matchName());
    return resolver.overrideSolutionInMatch(resolverCommand);
  }

  @NotNull
  private ResolverAction determineResolverAction(ChangeCondition changeCondition) {
    if (ChangeCondition.NO_CONDITION_FULFILLED.equals(changeCondition)) {
      return NOT_CHANGE;
    }
    boolean overflowed = counter.increaseAndCheckOverflow(changeCondition);
    return overflowed ? OVERRIDE : NOT_CHANGE;
  }
}