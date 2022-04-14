package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
class SelectAnalysisFeaturesQuery {

  private static final String QUERY =
      "SELECT aacf.feature,aaf.agent_config_feature_id,am.match_id,am.alert_id,aacf.agent_config"
          + " FROM ae_analysis aaa\n"
          + " JOIN ae_analysis_feature aaf ON aaa.analysis_id=aaf.analysis_id\n"
          + " JOIN ae_analysis_alert aaa2 ON aaa.analysis_id = aaa2.analysis_id\n"
          + " JOIN ae_match am ON am.alert_id =aaa2.alert_id\n"
          + " JOIN ae_agent_config_feature aacf ON "
          + " aaf.agent_config_feature_id = aacf.agent_config_feature_id\n"
          + " WHERE aaa.analysis_id IN (?) AND aaa2.alert_id IN (?)";
  private static final FeaturesRowMapper ROW_MAPPER = new FeaturesRowMapper();


  private final JdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<MatchFeatureDao> findAlertAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {
    return jdbcTemplate.query(QUERY, ROW_MAPPER, analysis, alerts);
  }

  private static final class FeaturesRowMapper
      implements RowMapper<MatchFeatureDao> {

    @Override
    public MatchFeatureDao mapRow(ResultSet rs, int rowNum) throws SQLException {
      var feature = rs.getString(1);
      var featureConfigId = rs.getLong(2);
      var matchId = rs.getLong(3);
      var alertId = rs.getLong(4);
      var agentConfig = rs.getString(5);
      return new MatchFeatureDao(alertId, matchId, featureConfigId, feature, agentConfig);
    }
  }
}
