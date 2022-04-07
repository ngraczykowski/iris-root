package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.qco.domain.model.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.silenteight.qco.domain.model.ResolverAction.NOT_CHANGE;
import static com.silenteight.qco.domain.model.ResolverAction.OVERRIDE;

@Component
@RequiredArgsConstructor
public class MatchProcessor {

  private final ChangeConditionFactory changeConditionFactory;
  private final QcoCounter counter;
  private final MatchResolver resolver;

  //TODO: add log info
  public MatchSolution processMatch(QcoRecommendationMatch match) {
    var changeCondition = changeConditionFactory
        .createChangeCondition(match.policyId(), match.stepId(), match.solution());
    var resolverAction = determineResolverAction(changeCondition);
    var resolverCommand = new ResolverCommand(match, changeCondition, resolverAction);
    return resolver.overrideSolutionMatch(resolverCommand);
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