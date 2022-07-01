/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputStorePublisherPort;
import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class CommentInputStorePublisher implements CommentInputStorePublisherPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final CommentInputDataAccess commentInputDataAccess;

  @Override
  public void resolve(CommentInput commentInput) {
    log.debug("Store comment input: {}", commentInput);
    inMemorySolvingExecutor.execute(() -> commentInputDataAccess.store(commentInput));
  }
}
