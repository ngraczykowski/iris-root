package com.silenteight.adjudication.engine.features.agentconfigfeature.data;

import com.silenteight.adjudication.api.v1.Analysis.Feature;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;
import java.util.stream.IntStream;

class InsertAgentConfigFeatureQuery {

  private final BatchSqlUpdate sql;

  InsertAgentConfigFeatureQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO ae_agent_config_feature(agent_config, feature, created_at)\n"
        + "VALUES (?, ?, now())\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("agentConfig", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("feature", Types.VARCHAR));

    sql.compile();
  }

  int execute(Collection<Feature> features) {
    features.forEach(acf -> update(acf.getAgentConfig(), acf.getFeature()));

    var rowsAffected = sql.flush();

    return IntStream.of(rowsAffected).sum();
  }

  private void update(String agentConfig, String feature) {
    sql.update(agentConfig, feature);
  }
}
