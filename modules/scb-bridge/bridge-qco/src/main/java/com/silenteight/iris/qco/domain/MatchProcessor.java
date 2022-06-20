/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.model.*;
import com.silenteight.qco.domain.model.*;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
    var resolverAction = determineResolverAction(changeCondition, match.onlyMark());
    log.debug("QCO module determined ResolverAction={} for {}",
        resolverAction.name(), match);
    var resolverCommand = new ResolverCommand(match, changeCondition, resolverAction);
    log.trace("QCO module is calling resolver for matchName={}", match.matchName());
    return resolver.overrideSolutionInMatch(resolverCommand);
  }

  @NotNull
  private ResolverAction determineResolverAction(ChangeCondition changeCondition) {
    if (ChangeCondition.NO_CONDITION_FULFILLED.equals(changeCondition)) {
      return ResolverAction.NOT_CHANGE;
    }
    boolean overflowed = counter.increaseAndCheckOverflow(changeCondition);
    return overflowed ? ResolverAction.OVERRIDE : ResolverAction.NOT_CHANGE;
  }

  @NotNull
  private ResolverAction determineResolverAction(ChangeCondition changeCondition, boolean mark) {
    ResolverAction resolverAction = determineResolverAction(changeCondition);
    if (mark && ResolverAction.OVERRIDE == resolverAction) {
      return ResolverAction.ONLY_MARK;
    }
    return resolverAction;
  }
}
