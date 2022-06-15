/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchSolutionPublisherPort;
import com.silenteight.adjudication.engine.solving.data.MatchSolutionStore;
import com.silenteight.adjudication.engine.solving.domain.MatchSolution;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class MatchSolutionPublisher implements MatchSolutionPublisherPort {

  private final Queue<MatchSolution> matchSolutionQueue;
  private final MatchSolutionStore matchSolutionStore;

  MatchSolutionPublisher(
      final MatchSolutionStore matchSolutionStore,
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<MatchSolution> matchSolutionQueue) {
    this.matchSolutionStore = matchSolutionStore;
    this.matchSolutionQueue = matchSolutionQueue;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 500, TimeUnit.MILLISECONDS);
  }

  private void process() {
    try {
      execute();
    } catch (Exception e) {
      log.error("Processing of match solution value failed: ", e);
    }
  }

  private void execute() {
    log.trace("Start process match solution value");
    while (true) {
      final var matchSolution = this.matchSolutionQueue.poll();
      if (matchSolution == null) {
        break;
      }
      matchSolutionStore.store(matchSolution);
    }
  }

  @Override
  public void resolve(MatchSolution matchSolution) {
    log.trace(
        "Store match solution analysisId={}, matchId={}",
        matchSolution.analysisId(),
        matchSolution.matchId());
    matchSolutionQueue.add(matchSolution);
  }
}
