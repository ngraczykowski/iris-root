package com.silenteight.adjudication.engine.comments.commentinput.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.comments.commentinput.CommentInputDataAccess;
import com.silenteight.adjudication.engine.comments.commentinput.domain.InsertCommentInputRequest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
class JdbcAlertCommentInputDataAccess implements CommentInputDataAccess {

  private final InsertCommentInputQuery insertCommentInputQuery;

  @Override
  @Transactional
  public void insertCommentInput(InsertCommentInputRequest alertCommentInput) {
    insertCommentInputQuery.insert(alertCommentInput);
  }
}
