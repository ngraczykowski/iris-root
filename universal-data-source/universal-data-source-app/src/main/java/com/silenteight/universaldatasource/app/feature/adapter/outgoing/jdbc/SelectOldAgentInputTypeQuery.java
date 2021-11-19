package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class SelectOldAgentInputTypeQuery {

  @Language("PostgreSQL")
  private static final String SQL =
      "SELECT COUNT(*) FROM uds_feature_input\n"
          + "WHERE agent_input_type NOT LIKE 'com.%'\n"
          + "LIMIT 1;";

  private final JdbcTemplate jdbcTemplate;

  Integer execute() {
    if (log.isDebugEnabled()) {
      log.debug("Updating agent input types");
    }

    return jdbcTemplate.queryForObject(SQL, Integer.class);
  }
}

