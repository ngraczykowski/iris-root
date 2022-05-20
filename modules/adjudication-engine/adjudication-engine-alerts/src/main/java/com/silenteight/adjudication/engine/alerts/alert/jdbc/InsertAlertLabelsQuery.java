package com.silenteight.adjudication.engine.alerts.alert.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.alerts.alert.domain.InsertLabelRequest;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
@RequiredArgsConstructor
class InsertAlertLabelsQuery {

  private final JdbcTemplate jdbcTemplate;
  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO ae_alert_labels(alert_id, name, value) VALUES (?, ?, ?)\n"
          + "ON CONFLICT DO NOTHING ";

  void insert(List<InsertLabelRequest> request) {
    var sql = createQuery();
    request.forEach(r -> sql.update(r.getAlertId(), r.getLabelName(), r.getLabelValue()));
    sql.flush();
  }

  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("alert_id", Types.BIGINT));
    sql.declareParameter(new SqlParameter("name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("value", Types.VARCHAR));

    sql.compile();

    return sql;
  }
}
