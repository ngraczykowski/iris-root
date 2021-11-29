package com.silenteight.universaldatasource.app.category.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
class DeleteCategoryValueQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "DELETE FROM uds_category_value\n"
          + " WHERE alert_name IN (:alerts)";

  private final NamedParameterJdbcTemplate jdbcTemplate;

  int execute(List<String> alerts) {
    var parameters = new MapSqlParameterSource(Map.of("alerts", alerts));
    return jdbcTemplate.update(SQL, parameters);
  }
}
