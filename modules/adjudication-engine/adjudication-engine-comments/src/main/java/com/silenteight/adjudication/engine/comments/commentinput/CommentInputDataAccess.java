package com.silenteight.adjudication.engine.comments.commentinput;

import com.silenteight.adjudication.engine.comments.commentinput.domain.InsertCommentInputRequest;

public interface CommentInputDataAccess {

  void insertCommentInput(InsertCommentInputRequest alertCommentInput);
}
