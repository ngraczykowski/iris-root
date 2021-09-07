package com.silenteight.payments.bridge.datasource.feature.adapter.outgoing.jdbc;

import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureInput;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.Collection;

class InsertFeatureQuery {

  private final BatchSqlUpdate sql;

  InsertFeatureQuery(JdbcTemplate jdbcTemplate) {
    sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql("INSERT INTO \n"
        + "pb_match_feature_input(match_name, feature, agent_input_type, agent_input)\n"
        + "VALUES (?, ?, ?, ?)\n"
        + "ON CONFLICT DO NOTHING");
    sql.declareParameter(new SqlParameter("match_name", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("feature", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("agent_input_type", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("agent_input", Types.OTHER));

    sql.compile();
  }

  void execute(Collection<MatchFeatureInput> matchFeatureInputs) {
    if (matchFeatureInputs.isEmpty())
      return;

    matchFeatureInputs.forEach(this::update);
    sql.flush();
  }

  private void update(MatchFeatureInput matchFeatureInput) {
    sql.update(
        matchFeatureInput.getMatch(), matchFeatureInput.getFeature(),
        matchFeatureInput.getAgentInputType(), matchFeatureInput.getAgentInput());
  }
}
