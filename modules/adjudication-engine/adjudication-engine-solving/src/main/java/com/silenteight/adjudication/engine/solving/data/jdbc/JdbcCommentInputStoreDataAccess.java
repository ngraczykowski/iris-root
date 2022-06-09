/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

@RequiredArgsConstructor
class JdbcCommentInputStoreDataAccess implements CommentInputDataAccess {

  private final CommentInputJdbcRepository commentInputJdbcRepository;

  public void store(CommentInput commentInput) {
    commentInputJdbcRepository.store(commentInput);
  }
}
