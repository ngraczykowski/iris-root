package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.ResolverCommand;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.silenteight.qco.domain.model.MatchSolution.OVERRIDDEN;
import static com.silenteight.qco.domain.model.MatchSolution.UNCHANGED;

@Slf4j
@Component
@RequiredArgsConstructor
class QueueMatchResolver implements MatchResolver {

  private final ProcessedMatchesRegister matchRegister;

  @Override
  public MatchSolution overrideSolutionMatch(ResolverCommand command) {
    //TODO: add log debug with args
    var matchSolution = createMatchSolution(command);
    if (matchSolution.changed()) {
      //TODO: add log info
      matchRegister.register(command.match());
    }
    return matchSolution;
  }

  private MatchSolution createMatchSolution(ResolverCommand command) {
    return switch (command.resolverAction()) {
      case OVERRIDE -> new MatchSolution(getTargetSolution(), OVERRIDDEN);
      case NOT_CHANGE -> new MatchSolution(command.match().solution(), UNCHANGED);
      case ONLY_MARK -> throw new UnsupportedOperationException();
    };
  }

  private String getTargetSolution() {
    //TODO: implement
    return StringUtils.EMPTY;
  }
}