package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class RemoveActionsQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM pb_learning_action pla\n"
          + " WHERE pla.learning_action_id IN (:action_id)\n";
  private static final String ACTION_ID = "action_id";

  private final JdbcTemplate jdbcTemplate;

  public void remove(List<Long> actionIds) {
    var query = createQuery();

    for (var actionId : actionIds) {
      var paramMap =
          Map.of(ACTION_ID, actionId);
      query.updateByNamedParam(paramMap);
    }

    query.flush();
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(ACTION_ID, Types.BIGINT));
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
