package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.comments.api.v2.CreatedCommentInput;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;
import com.silenteight.universaldatasource.app.commentinput.port.outgoing.CommentInputDataAccess;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Repository
class JdbcCommentInputDataAccess implements CommentInputDataAccess {

  private final InsertCommentInputsQuery insertCommentInputsQuery;

  private final StreamCommentInputQuery streamCommentInputQuery;

  private final SelectCommentInputsQuery selectCommentInputsQuery;

  @Override
  @Transactional
  public List<CreatedCommentInput> saveAll(
      List<AlertCommentInput> commentInputs) {
    return insertCommentInputsQuery.execute(commentInputs);
  }

  @Override
  public int stream(Collection<String> alerts, Consumer<AlertCommentInput> consumer) {
    return streamCommentInputQuery.execute(alerts, consumer);
  }

  @Override
  public List<AlertCommentInput> batchGetCommentInputs(List<String> alerts) {
    return selectCommentInputsQuery.execute(alerts);
  }
}
