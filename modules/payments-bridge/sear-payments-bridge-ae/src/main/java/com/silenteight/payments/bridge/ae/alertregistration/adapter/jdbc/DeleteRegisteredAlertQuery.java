package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class DeleteRegisteredAlertQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM pb_registered_alert\n"
          + " WHERE alert_name IN (:alert_name)\n"
          + " RETURNING alert_message_id";

  private static final String ALERT_NAME = "alert_name";
  private static final String ALERT_MESSAGE_ID = "alert_message_id";

  private final JdbcTemplate jdbcTemplate;

  List<UUID> execute(List<String> alertNames) {
    var batchSqlUpdate = createQuery();

    List<Map<String, Object>> keyList = new ArrayList<>();
    var keyHolder = new GeneratedKeyHolder();

    for (String alertName : alertNames) {
      var paramMap =
          Map.of(ALERT_NAME, alertName);
      batchSqlUpdate.updateByNamedParam(paramMap, keyHolder);
      keyList.addAll(keyHolder.getKeyList());
    }

    batchSqlUpdate.flush();

    return getCreatedCommentInputs(keyList);
  }

  private static List<UUID> getCreatedCommentInputs(
      List<Map<String, Object>> keyList) {
    return keyList
        .stream()
        .map(it -> UUID.fromString(it.get(ALERT_MESSAGE_ID).toString()))
        .collect(toList());
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(ALERT_NAME, Types.VARCHAR));
    batchSqlUpdate.setReturnGeneratedKeys(true);
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
