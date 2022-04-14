package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.ChangeCondition;
import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.ResolverCommand;

import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import static com.silenteight.qco.domain.model.MatchSolution.OVERRIDDEN;
import static com.silenteight.qco.domain.model.MatchSolution.UNCHANGED;

@Slf4j
@Component
@RequiredArgsConstructor
class MatchOverridingResolver implements MatchResolver {

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
      case NOT_CHANGE -> new MatchSolution(command.match().solution(), UNCHANGED);
      case ONLY_MARK -> throw new UnsupportedOperationException();
    };
  }

  @NotNull
  private MatchSolution createOverriddenMatchSolution(ResolverCommand command) {
    return Try.of(() ->
            new MatchSolution(getTargetSolution(command.changeCondition()), OVERRIDDEN))
        .onFailure(e -> log.error("Encounter error overriding match solution.", e))
        .getOrElse(new MatchSolution(command.match().solution(), UNCHANGED));
  }

  private String getTargetSolution(ChangeCondition changeCondition) {
    var qcoParams = configurationHolder.getConfiguration().get(changeCondition);
    if (qcoParams == null) {
      throw new IllegalStateException("The qcoParams value is required");
    }
    return qcoParams.targetSolution();
  }
}