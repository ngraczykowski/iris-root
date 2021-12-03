package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveDuplicatedAlertsQuery extends RemoveDuplicatedQuery {

  @Language("PostgreSQL")
  private static final String QUERY = "DELETE FROM\n"
      + "    pb_learning_alert lao\n"
      + "        USING pb_learning_alert lac\n"
      + "WHERE\n"
      + "    lao.learning_alert_id < lac.learning_alert_id\n"
      + "    AND lao.fkco_id = lac.fkco_id\n"
      + "    AND lao.fkco_v_system_id = lac.fkco_v_system_id;";

  private final JdbcTemplate jdbcTemplate;

  void remove() {
    jdbcTemplate.execute(QUERY);
  }
}
