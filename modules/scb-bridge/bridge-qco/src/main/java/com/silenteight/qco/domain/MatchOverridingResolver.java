package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.ChangeCondition;
import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;
import com.silenteight.qco.domain.model.ResolverCommand;
import com.silenteight.qco.infrastructure.CommentsPrefixProperties;

import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.silenteight.qco.domain.model.MatchSolution.OVERRIDDEN;
import static com.silenteight.qco.domain.model.MatchSolution.UNCHANGED;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Slf4j
@Component
@RequiredArgsConstructor
class MatchOverridingResolver implements MatchResolver {

  private final CommentsPrefixProperties properties;
  private final ProcessedMatchesRegister matchRegister;
  private final QcoConfigurationHolder configurationHolder;

  @Override
  public MatchSolution overrideSolutionInMatch(ResolverCommand command) {
    var matchSolution = createMatchSolution(command);
    log.debug("The resolver created {} for matchName={}",
        matchSolution.toString(), command.match().matchName());
    if (matchSolution.changed()) {
      log.info("The change of solution from {} to {} will be registered for matchName={}",
          command.match().solution(), matchSolution.solution(), command.match().matchName());
      matchRegister.register(command.match(), matchSolution);
    }
    return matchSolution;
  }

  private MatchSolution createMatchSolution(ResolverCommand command) {
    return switch (command.resolverAction()) {
      case OVERRIDE -> createOverriddenMatchSolution(command);
      case NOT_CHANGE -> createUnchangedMatchSolution(command.match());
      case ONLY_MARK -> throw new UnsupportedOperationException();
    };
  }

  @NotNull
  private MatchSolution createOverriddenMatchSolution(ResolverCommand command) {
    return Try.of(() ->
            new MatchSolution(
                getTargetSolution(command.changeCondition()),
                getTargetComment(command.match()),
                OVERRIDDEN))
        .onFailure(e -> log.error("Encounter error overriding match solution.", e))
        .getOrElse(createUnchangedMatchSolution(command.match()));
  }

  @NotNull
  private static MatchSolution createUnchangedMatchSolution(QcoRecommendationMatch match) {
    return new MatchSolution(match.solution(), match.comment(), UNCHANGED);
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
