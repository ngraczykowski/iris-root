package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
class CommentInputExtractor implements ResultSetExtractor<Integer> {

  private final Consumer<AlertCommentInput> consumer;

  @Override
  public Integer extractData(ResultSet rs) throws SQLException {
    var rowCount = 0;

    while (rs.next()) {
      rowCount++;
      AlertCommentInput alertCommentInput = AlertCommentInput.builder()
          .commentInputId(rs.getString(1))
          .alert(rs.getString(2))
          .commentInput(rs.getString(3))
          .matchCommentInputs(rs.getString(4))
          .build();

      consumer.accept(alertCommentInput);
    }

    return rowCount;
  }
}
