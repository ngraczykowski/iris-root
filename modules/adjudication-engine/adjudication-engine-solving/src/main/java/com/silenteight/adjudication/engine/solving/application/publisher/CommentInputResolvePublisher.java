package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.port.CommentInputResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputResolvePublisherPort;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class CommentInputResolvePublisher implements CommentInputResolvePublisherPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final CommentInputResolveProcessPort commentInputResolveProcessPort;

  public void resolve(final String alert) {
    log.debug("Resolve comment input: {}", alert);
    inMemorySolvingExecutor.execute(
        () -> commentInputResolveProcessPort.retrieveCommentInput(alert));
  }
}
