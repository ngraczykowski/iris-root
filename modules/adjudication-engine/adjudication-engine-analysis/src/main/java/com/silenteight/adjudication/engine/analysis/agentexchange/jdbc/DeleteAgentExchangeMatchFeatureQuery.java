package com.silenteight.adjudication.engine.analysis.agentexchange.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

class DeleteAgentExchangeMatchFeatureQuery {

  private final BatchSqlUpdate sql;

  DeleteAgentExchangeMatchFeatureQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("DELETE\n"
        + "FROM ae_agent_exchange_match_feature\n"
        + "WHERE array[CAST(agent_exchange_id AS VARCHAR), ?] IN (\n"
        + "    SELECT array[CAST(aae.agent_exchange_id AS VARCHAR), aacf.feature]\n"
        + "    FROM ae_agent_exchange_match_feature aaemf\n"
        + "             JOIN ae_agent_exchange aae\n"
        + "                  ON aae.agent_exchange_id = aaemf.agent_exchange_id\n"
        + "             JOIN ae_agent_config_feature aacf\n"
        + "                  ON aacf.agent_config_feature_id = aaemf.agent_config_feature_id\n"
        + "    WHERE aae.request_id = ?"
        + ")\n"
        + "  AND match_id = ?");
    sql.declareParameter(new SqlParameter("feature", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("requestId", Types.OTHER));
    sql.declareParameter(new SqlParameter("category", Types.BIGINT));

    sql.compile();
  }

  void execute(UUID agentExchangeRequestId, long matchId, List<String> features) {
    features.forEach(f -> sql.update(f, agentExchangeRequestId, matchId));
    sql.flush();
  }
}
