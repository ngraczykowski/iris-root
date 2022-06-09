/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchCategoryPublisherPort;
import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class MatchCategoryPublisher implements MatchCategoryPublisherPort {

  private final Queue<MatchCategory> alertCommentsInputQueue;
  private final MatchCategoryDataAccess matchCategoryDataAccess;

  MatchCategoryPublisher(
      final MatchCategoryDataAccess matchCategoryDataAccess,
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<MatchCategory> alertCommentsInputQueue) {
    this.matchCategoryDataAccess = matchCategoryDataAccess;
    this.alertCommentsInputQueue = alertCommentsInputQueue;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 500, TimeUnit.MILLISECONDS);
  }

  @Override
  public void resolve(MatchCategory matchCategory) {
    log.debug("Store comment input: {}", matchCategory);
    this.alertCommentsInputQueue.add(matchCategory);
  }

  private void process() {
    try {
      execute();
    } catch (Exception e) {
      log.error("Processing of comment input store failed: ", e);
    }
  }

  private void execute() {
    log.trace("Start process single comment input");
    while (true) {
      final var commentInput = this.alertCommentsInputQueue.poll();
      if (commentInput == null) {
        break;
      }
      matchCategoryDataAccess.store(commentInput);
    }
  }
}
