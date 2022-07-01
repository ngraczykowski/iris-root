/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchSolutionPublisherPort;
import com.silenteight.adjudication.engine.solving.data.MatchSolutionStore;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchSolutionPublisher implements MatchSolutionPublisherPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final MatchSolutionStore matchSolutionStore;

  @Override
  public void resolve(MatchSolution matchSolution) {
    log.trace(
        "Store match solution analysisId={}, matchId={}",
        matchSolution.analysisId(),
        matchSolution.matchId());
    inMemorySolvingExecutor.execute(() -> matchSolutionStore.store(matchSolution));
  }
}
