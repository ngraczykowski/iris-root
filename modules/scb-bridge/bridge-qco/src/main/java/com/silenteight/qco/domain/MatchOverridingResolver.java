package com.silenteight.qco.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.ResolverCommand;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class MatchOverridingResolver implements MatchResolver {

  private final ProcessedMatchesRegister matchRegister;
  private final MatchSolutionFactory matchSolutionFactory;

  @Override
  public MatchSolution overrideSolutionInMatch(ResolverCommand command) {
    var matchSolution = matchSolutionFactory.createMatchSolution(command);
    log.debug("The resolver created {} for matchName={}",
        matchSolution.toString(), command.match().matchName());
    if (matchSolution.qcoMarked()) {
      storeIntoDbAndSendToWarehouse(command, matchSolution);
    }
    return matchSolution;
  }

  private void storeIntoDbAndSendToWarehouse(ResolverCommand command, MatchSolution matchSolution) {
    log.info("The change of solution from {} to {} will be registered for matchName={}",
        command.match().solution(), matchSolution.solution(), command.match().matchName());
    matchRegister.register(command.match(), matchSolution);
  }
}
