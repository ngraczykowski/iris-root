package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
class DeleteOutdatedAgentExchangeMatchFeaturesQuery {

  private static final int DEFAULT_LIMIT = 4 * 1024 * 1024;

  @Language("PostgreSQL")
  public static final String SQL =
      "DELETE\n"
          + "FROM ae_agent_exchange_match_feature\n"
          + "WHERE agent_exchange_match_feature_id IN (\n"
          + "    SELECT agent_exchange_match_feature_id\n"
          + "    FROM ae_agent_exchange_match_feature\n"
          + "    WHERE created_at < (now() - interval '24 hours')\n"
          + "    LIMIT ?\n"
          + ")\n";

  private final JdbcTemplate jdbcTemplate;

  @Setter
  private int limit = DEFAULT_LIMIT;

  @Transactional
  int execute() {
    return jdbcTemplate.update(SQL, limit);
  }
}
