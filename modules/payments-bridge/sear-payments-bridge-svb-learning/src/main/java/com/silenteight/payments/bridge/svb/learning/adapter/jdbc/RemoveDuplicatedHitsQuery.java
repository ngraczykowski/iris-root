package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveDuplicatedHitsQuery extends RemoveDuplicatedQuery {

  @Language("PostgreSQL")
  private static final String QUERY = "DELETE FROM\n"
      + "    pb_learning_hit lho\n"
      + "        USING pb_learning_hit lhc\n"
      + "WHERE\n"
      + "    lho.learning_hit_id < lhc.learning_hit_id\n"
      + "    AND lho.fkco_messages = lhc.fkco_messages\n"
      + "    AND lho.fkco_v_matched_tag = lhc.fkco_v_matched_tag\n"
      + "    AND lho.fkco_i_sequence = lhc.fkco_i_sequence;";

  private final JdbcTemplate jdbcTemplate;

  void remove() {
    jdbcTemplate.execute(QUERY);
  }
}
