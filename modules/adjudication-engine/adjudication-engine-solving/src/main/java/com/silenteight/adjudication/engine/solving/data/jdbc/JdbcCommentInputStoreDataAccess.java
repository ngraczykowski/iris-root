/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
class JdbcCommentInputStoreDataAccess implements CommentInputDataAccess {

  private final CommentInputJdbcRepository commentInputJdbcRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void store(CommentInput commentInput) {
    commentInputJdbcRepository.store(commentInput);
  }
}
