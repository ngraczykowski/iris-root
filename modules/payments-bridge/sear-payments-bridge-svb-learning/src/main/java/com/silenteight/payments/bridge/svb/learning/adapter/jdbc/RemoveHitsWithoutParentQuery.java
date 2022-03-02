package com.silenteight.payments.bridge.svb.learning.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RemoveHitsWithoutParentQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM pb_learning_hit plh\n"
          + " WHERE plh.fkco_messages NOT IN (SELECT fkco_id from pb_learning_alert)\n";

  private final JdbcTemplate jdbcTemplate;

  public void remove() {
    jdbcTemplate.update(SQL);
  }
}
