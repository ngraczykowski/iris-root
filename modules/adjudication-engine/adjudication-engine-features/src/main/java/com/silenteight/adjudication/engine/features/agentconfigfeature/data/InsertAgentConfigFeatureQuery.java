package com.silenteight.adjudication.engine.features.agentconfigfeature.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.Collection;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
class InsertAgentConfigFeatureQuery {

  private final JdbcTemplate jdbcTemplate;

  private static final String SQL =
      "INSERT INTO ae_agent_config_feature(agent_config, feature, created_at)\n"
          + "VALUES (?, ?, now())\n"
          + "ON CONFLICT DO NOTHING";

  int execute(Collection<Feature> features) {
    var sql = createQuery();
    features.forEach(acf -> sql.update(acf.getAgentConfig(), acf.getFeature()));

    var rowsAffected = sql.flush();

    return IntStream.of(rowsAffected).sum();
  }


  private BatchSqlUpdate createQuery() {
    var sql = new BatchSqlUpdate();

    sql.setJdbcTemplate(jdbcTemplate);
    sql.setSql(SQL);
    sql.declareParameter(new SqlParameter("agentConfig", Types.VARCHAR));
    sql.declareParameter(new SqlParameter("feature", Types.VARCHAR));

    sql.compile();

    return sql;
  }
}
