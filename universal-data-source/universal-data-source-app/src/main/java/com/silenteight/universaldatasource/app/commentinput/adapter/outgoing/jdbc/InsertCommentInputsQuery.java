package com.silenteight.universaldatasource.app.commentinput.adapter.outgoing.jdbc;

import com.silenteight.datasource.comments.api.v2.CreatedCommentInput;
import com.silenteight.universaldatasource.app.commentinput.model.AlertCommentInput;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InsertCommentInputsQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO uds_comment_input(alert_name, alert_comment_input, match_comment_inputs)\n"
          + " VALUES (:alert_name, :alert_comment_input, :match_comment_inputs)\n"
          + " ON CONFLICT DO NOTHING\n"
          + "RETURNING comment_input_id, alert_name";

  private static final String ALERT_NAME = "alert_name";
  private static final String ALERT_COMMENT_INPUT = "alert_comment_input";
  private static final String MATCH_COMMENT_INPUT = "match_comment_inputs";

  private final BatchSqlUpdate batchSqlUpdate;

  InsertCommentInputsQuery(JdbcTemplate jdbcTemplate) {
    batchSqlUpdate = new BatchSqlUpdate();

    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(ALERT_NAME, Types.VARCHAR));
    batchSqlUpdate.declareParameter(new SqlParameter(ALERT_COMMENT_INPUT, Types.OTHER));
    batchSqlUpdate.declareParameter(new SqlParameter(MATCH_COMMENT_INPUT, Types.OTHER));
    batchSqlUpdate.setReturnGeneratedKeys(true);

    batchSqlUpdate.compile();
  }

  private static List<CreatedCommentInput> getCreatedCommentInputs(
      List<Map<String, Object>> keyList) {
    return keyList
        .stream()
        .map(it -> CreatedCommentInput
            .newBuilder()
            .setName("comment-inputs/" + it.get("comment_input_id").toString())
            .setAlert(it.get(ALERT_NAME).toString())
            .build()).collect(Collectors.toList());
  }

  List<CreatedCommentInput> execute(List<AlertCommentInput> commentInputs) {
    List<Map<String, Object>> keyList = new ArrayList<>();
    var keyHolder = new GeneratedKeyHolder();
    for (AlertCommentInput alertCommentInput : commentInputs) {
      update(alertCommentInput, keyHolder);
      keyList.addAll(keyHolder.getKeyList());
    }
    batchSqlUpdate.flush();

    return getCreatedCommentInputs(keyList);
  }

  private void update(AlertCommentInput alertCommentInput, KeyHolder keyHolder) {
    var paramMap =
        Map.of(ALERT_NAME, alertCommentInput.getAlert(),
            ALERT_COMMENT_INPUT, alertCommentInput.getCommentInput(),
            MATCH_COMMENT_INPUT, alertCommentInput.getMatchCommentInputs()
        );
    batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
  }
}
