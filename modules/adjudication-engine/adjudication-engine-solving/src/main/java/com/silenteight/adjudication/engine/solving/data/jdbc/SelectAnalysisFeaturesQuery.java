package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureDao;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Component
class SelectAnalysisFeaturesQuery {

  @Language("PostgreSQL")
  private static final String QUERY =
      "SELECT aacf.feature,\n"
          + "       aaf.agent_config_feature_id,\n"
          + "       am.match_id,\n"
          + "       am.alert_id,\n"
          + "       aacf.agent_config,\n"
          + "       aaa.policy,\n"
          + "       aaa.strategy,\n"
          + "       aaa.analysis_id\n"
          + "FROM ae_analysis aaa\n"
          + "         JOIN ae_analysis_feature aaf ON aaa.analysis_id = aaf.analysis_id\n"
          + "         JOIN ae_analysis_alert aaa2 ON aaa.analysis_id = aaa2.analysis_id\n"
          + "         JOIN ae_match am ON am.alert_id = aaa2.alert_id\n"
          + "         JOIN ae_agent_config_feature aacf\n"
          + "              ON "
          + "aaf.agent_config_feature_id = aacf.agent_config_feature_id\n"
          + "WHERE aaa.analysis_id IN (:analysis)\n"
          + "  AND aaa2.alert_id IN (:alerts)";
  private static final FeaturesRowMapper ROW_MAPPER = new FeaturesRowMapper();


  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<MatchFeatureDao> findAlertAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {

    var parameters = new MapSqlParameterSource(Map.of(
        "analysis", analysis,
        "alerts", alerts
    ));
    return jdbcTemplate.query(QUERY, parameters, ROW_MAPPER);
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
      var policy = rs.getString(6);
      var strategy = rs.getString(7);
      var analysisId = rs.getLong(8);
      return new MatchFeatureDao(analysisId,
          alertId, matchId, featureConfigId, feature, agentConfig, policy, strategy);
    }
  }
}
