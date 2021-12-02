package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveDuplicatedActionsQuery extends RemoveDuplicatedQuery {

  @Language("PostgreSQL")
  private static final String QUERY = "DELETE FROM\n"
      + "    pb_learning_action lao\n"
      + "        USING pb_learning_action lac\n"
      + "WHERE\n"
      + "    lao.learning_action_id < lac.learning_action_id\n"
      + "    AND lao.fkco_messages = lac.fkco_messages\n"
      + "    AND lao.fkco_d_action_datetime = lac.fkco_d_action_datetime;";

  private final JdbcTemplate jdbcTemplate;

  @Override
  void remove() {
    jdbcTemplate.execute(QUERY);
  }
}
