package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveDuplicatedActionStatusQuery extends RemoveDuplicatedQuery {

  @Language("PostgreSQL")
  private static final String QUERY = "DELETE FROM\n"
      + "    pb_learning_action_status laso\n"
      + "        USING pb_learning_action_status lasc\n"
      + "WHERE\n"
      + "    laso.learning_action_status_id < lasc.learning_action_status_id\n"
      + "    AND laso.fkco_id = lasc.fkco_id\n"
      + "    AND laso.fkco_v_status_name = lasc.fkco_v_status_name\n"
      + "    AND laso.fkco_v_status_behavior = lasc.fkco_v_status_behavior;";

  private final JdbcTemplate jdbcTemplate;

  void remove() {
    jdbcTemplate.execute(QUERY);
  }
}
