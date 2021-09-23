package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Component
class StreamCommentInputQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT comment_input_id, alert, alert_comment_input, match_comment_inputs\n"
          + " FROM uds_comment_input\n"
          + " WHERE alert IN (:alertNames)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  int execute(Collection<String> alerts, Consumer<AlertCommentInput> consumer) {

    var parameters = new MapSqlParameterSource("alertNames", alerts);
    var commentInputs = jdbcTemplate.query(SQL, parameters, new CommentInputExtractor(consumer));
    return commentInputs != null ? commentInputs : 0;
  }
}
