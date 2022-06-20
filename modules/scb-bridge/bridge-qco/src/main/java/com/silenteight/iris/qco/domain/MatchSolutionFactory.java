/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.model.ChangeCondition;
import com.silenteight.iris.qco.domain.model.MatchSolution;
import com.silenteight.iris.qco.domain.model.QcoRecommendationMatch;
import com.silenteight.iris.qco.domain.model.ResolverCommand;
import com.silenteight.iris.qco.infrastructure.CommentsPrefixProperties;

import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Slf4j
@Component
@RequiredArgsConstructor
class MatchSolutionFactory {

  private final CommentsPrefixProperties properties;
  private final QcoConfigurationHolder configurationHolder;

  MatchSolution createMatchSolution(ResolverCommand command) {
    return switch (command.resolverAction()) {
      case OVERRIDE -> createOverriddenMatchSolution(command);
      case NOT_CHANGE -> createUnchangedMatchSolution(command.match());
      case ONLY_MARK -> createOnlyMarkMatchSolution(command);
    };
  }

  private MatchSolution createOnlyMarkMatchSolution(ResolverCommand command) {
    return new MatchSolution(
        command.match().solution(),
        command.match().comment(),
        MatchSolution.QCO_MARKED);
  }

  @NotNull
  private MatchSolution createOverriddenMatchSolution(ResolverCommand command) {
    return Try.of(() ->
            new MatchSolution(
                getTargetSolution(command.changeCondition()),
                getTargetComment(command.match()),
                MatchSolution.QCO_MARKED))
        .onFailure(e -> log.error("Encounter error overriding match solution.", e))
        .getOrElse(createUnchangedMatchSolution(command.match()));
  }

  @NotNull
  private static MatchSolution createUnchangedMatchSolution(QcoRecommendationMatch match) {
    return new MatchSolution(match.solution(), match.comment(), MatchSolution.QCO_UNMARKED);
  }

  private String getTargetSolution(ChangeCondition changeCondition) {
    var qcoParams = configurationHolder.getConfiguration().get(changeCondition);
    if (qcoParams == null) {
      throw new IllegalStateException("The qcoParams value is required");
    }
    return qcoParams.targetSolution();
  }

  @NotNull
  private String getTargetComment(QcoRecommendationMatch match) {
    return String.join(SPACE, properties.commentsPrefix(), defaultString(match.comment()));
  }
}
