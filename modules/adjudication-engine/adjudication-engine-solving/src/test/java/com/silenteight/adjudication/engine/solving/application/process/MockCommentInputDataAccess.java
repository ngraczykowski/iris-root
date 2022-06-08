/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.data.CommentInputDataAccess;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

import java.util.ArrayList;
import java.util.List;

class MockCommentInputDataAccess implements CommentInputDataAccess {

  private List<CommentInput> saved = new ArrayList<>();

  @Override
  public void store(CommentInput commentInput) {
    saved.add(commentInput);
  }

  public int getSavedCount() {
    return saved.size();
  }
}
