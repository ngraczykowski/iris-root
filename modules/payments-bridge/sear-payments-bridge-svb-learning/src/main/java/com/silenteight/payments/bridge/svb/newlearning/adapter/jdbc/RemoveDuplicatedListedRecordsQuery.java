package com.silenteight.payments.bridge.svb.newlearning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveDuplicatedListedRecordsQuery extends RemoveDuplicatedQuery {

  @Language("PostgreSQL")
  private static final String QUERY = "DELETE FROM\n"
      + "    pb_learning_listed_record llro\n"
      + "        USING pb_learning_listed_record llrc\n"
      + "WHERE\n"
      + "    llro.learning_listed_record_id < llrc.learning_listed_record_id\n"
      + "    AND llro.fkco_id = llrc.fkco_id\n"
      + "    AND llro.fkco_v_list_fmm_id = llrc.fkco_v_list_fmm_id;";

  private final JdbcTemplate jdbcTemplate;

  void remove() {
    jdbcTemplate.execute(QUERY);
  }
}
