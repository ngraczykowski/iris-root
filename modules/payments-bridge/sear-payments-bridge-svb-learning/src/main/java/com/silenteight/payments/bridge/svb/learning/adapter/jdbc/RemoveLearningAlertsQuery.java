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
class RemoveLearningAlertsQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM pb_learning_alert pla\n"
          + " WHERE pla.learning_alert_id IN (:alert_id)\n";
  private static final String ALERTS_ID = "alert_id";

  private final JdbcTemplate jdbcTemplate;

  public void remove(List<Long> alertIds) {
    var query = createQuery();

    for (var alertId : alertIds) {
      var paramMap =
          Map.of(ALERTS_ID, alertId);
      query.updateByNamedParam(paramMap);
    }

    query.flush();
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(ALERTS_ID, Types.BIGINT));
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
