package com.silenteight.adjudication.engine.solve.agentconfigfeature.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;

class InsertAgentConfigFeatureQuery {

  private final BatchSqlUpdate sql;

  InsertAgentConfigFeatureQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_agent_config_feature(agent_config, feature, created_at)"
        + " VALUES (?, ?, now())"
        + " ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("agentConfig", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("feature", Types.VARCHAR));

    sql.compile();
  }

  void execute(String agentConfig, String feature) {
    sql.update(agentConfig, feature);
  }

  int[] flush() {
    return sql.flush();
  }
}
