package com.silenteight.adjudication.engine.analysis.analysis.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Component
class SelectAnalysisAgentConfigQuery {

  private static final AgentConfigRowMapper ROW_MAPPER = new AgentConfigRowMapper();
  private final JdbcTemplate jdbcTemplate;

  List<String> execute(long analysisId) {
    return jdbcTemplate.query(
        "SELECT agent_config FROM ae_analysis_agent_config_query WHERE analysis_id = ?",
        ROW_MAPPER, analysisId);
  }

  private static final class AgentConfigRowMapper implements RowMapper<String> {

    @Override
    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
      return rs.getString(1);
    }
  }
}
