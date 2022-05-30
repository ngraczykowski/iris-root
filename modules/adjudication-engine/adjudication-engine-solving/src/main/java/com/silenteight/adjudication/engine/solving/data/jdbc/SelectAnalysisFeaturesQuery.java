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
        """
          SELECT aacf.feature,
                 aaf.agent_config_feature_id,
                 am.match_id,
                 am.alert_id,
                 aacf.agent_config,
                 aaa.policy,
                 aaa.strategy,
                 aaa.analysis_id,
                 amfv.value,
                 amfv.reason,
                 am.client_match_identifier,
                 ac.category,
                 amcv.value as category_value
          FROM ae_analysis aaa
                   LEFT JOIN ae_analysis_feature aaf ON aaa.analysis_id = aaf.analysis_id
                   LEFT Join ae_analysis_category aac on aaa.analysis_id = aac.analysis_id
                   LEFT JOIN ae_category ac on aac.category_id = ac.category_id
                   JOIN ae_analysis_alert aaa2 ON aaa.analysis_id = aaa2.analysis_id
                   JOIN ae_match am ON am.alert_id = aaa2.alert_id
                   LEFT JOIN ae_agent_config_feature aacf
                        ON aaf.agent_config_feature_id = aacf.agent_config_feature_id
                  LEFT JOIN ae_match_feature_value amfv ON am.match_id = amfv.match_id
                  LEFT JOIN ae_match_category_value amcv ON am.match_id = amcv.match_id
          WHERE aaa.analysis_id IN (:analysis)
            AND aaa2.alert_id IN (:alerts)
        """;
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
      var feature = rs.getString("feature");
      var featureConfigId = rs.getLong("agent_config_feature_id");
      var matchId = rs.getLong("match_id");
      var alertId = rs.getLong("alert_id");
      var agentConfig = rs.getString("agent_config");
      var policy = rs.getString("policy");
      var strategy = rs.getString("strategy");
      var analysisId = rs.getLong("analysis_id");
      var featureValue = rs.getString("value");
      var featureReason = rs.getString("reason");
      var clientMatchId = rs.getString("client_match_identifier");
      var category = rs.getString("category");
      var categoryValue = rs.getString("category_value");

      return MatchFeatureDao
          .builder()
          .analysisId(analysisId)
          .alertId(alertId)
          .matchId(matchId)
          .agentConfigFeatureId(featureConfigId)
          .feature(feature)
          .agentConfig(agentConfig)
          .policy(policy)
          .strategy(strategy)
          .featureValue(featureValue)
          .featureReason(featureReason)
          .clientMatchId(clientMatchId)
          .category(category)
          .categoryValue(categoryValue)
          .build();
    }
  }
}
