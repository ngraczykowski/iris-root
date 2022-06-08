package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.port.CommentInputResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputResolvePublisherPort;

import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
class CommentInputResolvePublisher implements CommentInputResolvePublisherPort {

  private final Queue<String> alertCommentsInputQueue;
  private final CommentInputResolveProcessPort commentInputResolveProcessPort;

  CommentInputResolvePublisher(
      CommentInputResolveProcessPort commentInputResolveProcessPort,
      final ScheduledExecutorService scheduledExecutorService,
      final Queue<String> alertCommentsInputQueue) {
    this.alertCommentsInputQueue = alertCommentsInputQueue;
    this.commentInputResolveProcessPort = commentInputResolveProcessPort;
    scheduledExecutorService.scheduleAtFixedRate(this::process, 10, 10, TimeUnit.MILLISECONDS);
  }

  public void resolve(final String alert) {
    log.debug("Resolve comment input: {}", alert);
    this.alertCommentsInputQueue.add(alert);
  }

  private void process() {
    try {
      execute();
    } catch (Exception e) {
      log.error("Processing of category value failed: ", e);
    }
  }

  private void execute() {
    log.trace("Start process single alert with received comment input");
    while (true) {
      final String alertID = this.alertCommentsInputQueue.poll();
      if (alertID == null) {
        break;
      }
      commentInputResolveProcessPort.retrieveCommentInput(alertID);
    }
  }
}
