package com.silenteight.adjudication.engine.features.agentconfigfeature.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
class SelectAgentConfigFeatureQuery {

  private static final AgentConfigFeatureDtoRowMapper ROW_MAPPER =
      new AgentConfigFeatureDtoRowMapper();

  private final JdbcTemplate jdbcTemplate;

  List<AgentConfigFeatureDto> findAllByNamesIn(List<Feature> names) {
    // FIXME(ahaczewski): Replace with NamedParameterized query with list in WHERE IN (...).
    jdbcTemplate.execute(
        "CREATE TEMP TABLE IF NOT EXISTS tmp_features (name VARCHAR(300))"
            + " ON COMMIT DELETE ROWS");

    jdbcTemplate.batchUpdate(
        "INSERT INTO tmp_features VALUES (?)",
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            var name = names.get(i);

            ps.setString(1, name.getAgentConfig() + "/" + name.getFeature());
          }

          @Override
          public int getBatchSize() {
            return names.size();
          }
        });

    var agentConfigFeatures = jdbcTemplate.query(
        "SELECT agent_config_feature_id, agent_config, feature"
            + " FROM ae_agent_config_feature"
            + " WHERE concat(agent_config, '/', feature) IN (SELECT name FROM tmp_features)",
        ROW_MAPPER);

    jdbcTemplate.execute("DELETE FROM tmp_features");

    return agentConfigFeatures;
  }

  private static final class AgentConfigFeatureDtoRowMapper
      implements RowMapper<AgentConfigFeatureDto> {

    @Override
    public AgentConfigFeatureDto mapRow(ResultSet rs, int rowNum) throws SQLException {
      var id = rs.getLong(1);
      var agentConfig = rs.getString(2);
      var feature = rs.getString(3);

      return new AgentConfigFeatureDto(id, agentConfig, feature);
    }
  }
}
