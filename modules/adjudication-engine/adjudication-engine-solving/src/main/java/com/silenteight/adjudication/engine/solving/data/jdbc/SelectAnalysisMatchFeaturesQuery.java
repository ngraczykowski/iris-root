/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class SelectAnalysisMatchFeaturesQuery {
  private final NamedParameterJdbcTemplate jdbcTemplate;
  @Language("PostgreSQL")
  private static final String QUERY = """
          SELECT aa.policy,
                 aa.strategy,
                 aa.analysis_id,
                 aaa.alert_id,
                 al.priority,
                 am.match_id,
                 am.client_match_identifier,
                 aaf.agent_config_feature_id,
                 aacf.agent_config,
                 aacf.feature,
                 amfv.value,
                 amfv.reason
          FROM ae_analysis aa
                   LEFT JOIN ae_analysis_feature aaf ON aaf.analysis_id = aa.analysis_id
                   JOIN ae_analysis_alert aaa ON aaa.analysis_id = aa.analysis_id
                   JOIN ae_alert al on al.alert_id=aaa.alert_id
                   JOIN ae_match am ON am.alert_id = aaa.alert_id
                   LEFT JOIN ae_agent_config_feature aacf
                             ON aacf.agent_config_feature_id = aaf.agent_config_feature_id
                   LEFT JOIN ae_match_feature_value amfv
                             ON amfv.match_id = am.match_id AND
                                aaf.agent_config_feature_id =
                                amfv.agent_config_feature_id
          WHERE aa.analysis_id = :analysis
             AND aaa.alert_id = :alert
          """;

  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public List<MatchFeatureDao> findAlertMatchFeatures(Long analysis, Long alert) {

    var parameters = new MapSqlParameterSource(Map.of(
        "analysis", analysis,
        "alert", alert
    ));
    var rowMapper = new MatchAggregateRowMapper();
    return jdbcTemplate.query(QUERY, parameters, rowMapper);
  }

  @Getter
  private static class MatchAggregateRowMapper
      implements RowMapper<MatchFeatureDao> {

    @Override
    public MatchFeatureDao mapRow(ResultSet rs, int rowNum) throws SQLException {
      var priority = rs.getInt("priority");
      var policy = rs.getString("policy");
      var strategy = rs.getString("strategy");
      var featureName = rs.getString("feature");
      var featureConfigId = rs.getLong("agent_config_feature_id");
      var matchId = rs.getLong("match_id");
      var agentConfig = rs.getString("agent_config");
      var featureValue = rs.getString("value");
      var featureReason = rs.getString("reason");
      var clientMatchId = rs.getString("client_match_identifier");

      return
          new MatchFeatureDao(priority,policy,strategy,matchId, clientMatchId,featureConfigId,
              featureName, agentConfig, featureValue, featureReason);
    }
  }
}
