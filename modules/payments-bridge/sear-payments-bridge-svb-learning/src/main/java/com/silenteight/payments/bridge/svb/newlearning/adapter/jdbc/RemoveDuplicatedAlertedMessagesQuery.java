package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveDuplicatedAlertedMessagesQuery extends RemoveDuplicatedQuery {

  @Language("PostgreSQL")
  private static final String QUERY = "DELETE FROM\n"
      + "    pb_learning_alerted_message lamo\n"
      + "        USING pb_learning_alerted_message lamc\n"
      + "WHERE\n"
      + "    lamo.learning_alerted_message_id < lamc.learning_alerted_message_id\n"
      + "    AND lamo.fkco_messages = lamc.fkco_messages\n"
      + "    AND lamo.fkco_i_blockinghits = lamc.fkco_i_blockinghits\n"
      + "    AND lamo.fkco_d_filtered_datetime = lamc.fkco_d_filtered_datetime;";

  private final JdbcTemplate jdbcTemplate;

  void remove() {
    jdbcTemplate.execute(QUERY);
  }
}
