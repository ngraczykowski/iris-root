package com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;

class InsertAgentConfigFeatureQuery extends BatchSqlUpdate {

  InsertAgentConfigFeatureQuery() {
    initializeSql();
  }

  private void initializeSql() {
    setSql("INSERT INTO ae_agent_config_feature(agent_config, feature, created_at)"
        + " VALUES (?, ?, now())"
        + " ON CONFLICT DO NOTHING");
    declareParameter(new SqlParameter("agentConfig", Types.VARCHAR));
    declareParameter(new SqlParameter("feature", Types.VARCHAR));
  }

  void execute(String agentConfig, String feature) {
    update(agentConfig, feature);
  }
}
