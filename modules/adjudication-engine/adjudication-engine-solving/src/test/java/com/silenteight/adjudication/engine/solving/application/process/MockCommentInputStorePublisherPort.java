/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.port.CommentInputStorePublisherPort;
import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

public class MockCommentInputStorePublisherPort implements CommentInputStorePublisherPort {

  private final MockCommentInputDataAccess mockCommentInputClient;

  MockCommentInputStorePublisherPort() {
    this.mockCommentInputClient = new MockCommentInputDataAccess();
  }

  @Override
  public void resolve(CommentInput commentInput) {
    mockCommentInputClient.store(commentInput);
  }

  public int getSavedCount() {
    return mockCommentInputClient.getSavedCount();
  }
}
