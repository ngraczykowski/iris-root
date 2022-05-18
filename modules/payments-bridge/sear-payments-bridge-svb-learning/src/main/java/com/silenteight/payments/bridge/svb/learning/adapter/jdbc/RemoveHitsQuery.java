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
class RemoveHitsQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM pb_learning_hit plh\n"
          + " WHERE plh.learning_hit_id IN (:hit_id)\n";
  private static final String HIT_ID = "hit_id";

  private final JdbcTemplate jdbcTemplate;

  public void remove(List<Long> hitIds) {
    var query = createQuery();

    for (var hitId : hitIds) {
      var paramMap =
          Map.of(HIT_ID, hitId);
      query.updateByNamedParam(paramMap);
    }

    query.flush();
  }

  private BatchSqlUpdate createQuery() {
    var batchSqlUpdate = new BatchSqlUpdate();
    batchSqlUpdate.setJdbcTemplate(jdbcTemplate);
    batchSqlUpdate.setSql(SQL);
    batchSqlUpdate.declareParameter(new SqlParameter(HIT_ID, Types.BIGINT));
    batchSqlUpdate.compile();
    return batchSqlUpdate;
  }
}
