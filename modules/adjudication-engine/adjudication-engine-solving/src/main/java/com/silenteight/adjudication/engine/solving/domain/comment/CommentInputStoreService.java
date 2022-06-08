/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.domain.comment;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;

@RequiredArgsConstructor
public class CommentInputStoreService {

  private final CommentInputJdbcRepository commentInputJdbcRepository;

  @Async
  public void store(CommentInput commentInput) {
    commentInputJdbcRepository.store(commentInput);
  }
}
