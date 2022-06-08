/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data;

import com.silenteight.adjudication.engine.solving.domain.comment.CommentInput;

public interface CommentInputDataAccess {

  void store(CommentInput commentInput);
}
