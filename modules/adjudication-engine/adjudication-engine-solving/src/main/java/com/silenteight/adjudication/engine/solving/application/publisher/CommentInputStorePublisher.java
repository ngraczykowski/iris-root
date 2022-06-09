/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputStorePublisherPort;
import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CommentInputStorePublisher implements CommentInputStorePublisherPort {

  private final Queue<CommentInput> alertCommentsInputQueue;
  private final CommentInputDataAccess commentInputDataAccess;

  CommentInputStorePublisher(
      final CommentInputDataAccess commentInputDataAccess,
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<CommentInput> alertCommentsInputQueue) {
    this.alertCommentsInputQueue = alertCommentsInputQueue;

    this.commentInputDataAccess = commentInputDataAccess;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 500, TimeUnit.MILLISECONDS);
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
      commentInputDataAccess.store(commentInput);
    }
  }

  @Override
  public void resolve(CommentInput commentInput) {
    log.debug("Store comment input: {}", commentInput);
    this.alertCommentsInputQueue.add(commentInput);
  }
}
