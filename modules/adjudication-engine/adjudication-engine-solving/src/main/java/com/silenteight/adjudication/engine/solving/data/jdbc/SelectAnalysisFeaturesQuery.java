package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.*;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
class SelectAnalysisFeaturesQuery {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Language("PostgreSQL")
  private static final String QUERY =
      """
          SELECT aacf.feature,
                 aaf.agent_config_feature_id,
                 am.match_id,
                 am.alert_id,
                 a.priority,
                 aacf.agent_config,
                 aaa.policy,
                 aaa.strategy,
                 aaa.analysis_id,
                 amfv.value,
                 amfv.reason,
                 am.client_match_identifier,
                 ac.category,
                 amcv.value as category_value,
                 aal.name as label_name,
                 aal.value as label_value
          FROM ae_analysis aaa
                   LEFT JOIN ae_analysis_feature aaf ON aaa.analysis_id = aaf.analysis_id
                   LEFT Join ae_analysis_category aac on aaa.analysis_id = aac.analysis_id
                   LEFT JOIN ae_category ac on aac.category_id = ac.category_id
                   JOIN ae_analysis_alert aaa2 ON aaa.analysis_id = aaa2.analysis_id
                   JOIN ae_alert a ON a.alert_id=aaa2.alert_id
                   JOIN ae_match am ON am.alert_id = aaa2.alert_id
                   LEFT JOIN ae_agent_config_feature aacf
                        ON aaf.agent_config_feature_id = aacf.agent_config_feature_id
                  LEFT JOIN ae_match_feature_value amfv ON am.match_id = amfv.match_id
                  LEFT JOIN ae_match_category_value amcv ON am.match_id = amcv.match_id
                  LEFT JOIN ae_alert_labels aal ON aaa2.alert_id = aal.alert_id
          WHERE aaa.analysis_id IN (:analysis)
            AND aaa2.alert_id IN (:alerts)
                """;


  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public Map<Long, AlertAggregate> findAlertAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {

    var parameters = new MapSqlParameterSource(Map.of(
        "analysis", analysis,
        "alerts", alerts
    ));
    var rowMapper = new AlertAggregateRowMapper();
    jdbcTemplate.query(QUERY, parameters, rowMapper);
    return rowMapper.getAlerts();
  }

  @Getter
  private static class AlertAggregateRowMapper
      implements RowMapper<AlertDao> {

    private final Map<Long, AlertAggregate> alerts = new HashMap<>();

    @Override
    public AlertDao mapRow(ResultSet rs, int rowNum) throws SQLException {
      var featureName = rs.getString("feature");
      var featureConfigId = rs.getLong("agent_config_feature_id");
      var matchId = rs.getLong("match_id");
      var alertId = rs.getLong("alert_id");
      var priority = rs.getInt("priority");
      var agentConfig = rs.getString("agent_config");
      var policy = rs.getString("policy");
      var strategy = rs.getString("strategy");
      var analysisId = rs.getLong("analysis_id");
      var featureValue = rs.getString("value");
      var featureReason = rs.getString("reason");
      var clientMatchId = rs.getString("client_match_identifier");
      var categoryName = rs.getString("category");
      var categoryValue = rs.getString("category_value");
      var labelName = rs.getString("label_name");
      var labelValue = rs.getString("label_value");

      var alertAggregate = alerts.getOrDefault(
          alertId,
          new AlertAggregate(
              analysisId,
              alertId,
              priority,
              new HashMap<>(),
              policy,
              strategy,
              new HashMap<>(),
              new HashMap<>()));
      var labels = alertAggregate.labels();
      var agentConfigs = alertAggregate.agentFeatures();
      var agent = agentConfigs.getOrDefault(agentConfig, new HashSet<>());
      agent.add(featureName);
      var matches = alertAggregate.matches();
      var match = matches.getOrDefault(
          matchId,
          new MatchAggregate(matchId, clientMatchId, new HashMap<>(), new HashMap<>()));
      var features = match.features();
      var feature = features.getOrDefault(
          featureName,
          new FeatureAggregate(featureConfigId,
              featureName, agentConfig, featureValue, featureReason));
      var categories = match.categories();
      var category = categories.getOrDefault(
          categoryName,
          new CategoryAggregate(categoryName, categoryValue));

      if (featureName != null) {
        features.putIfAbsent(featureName, feature);
      }
      if (categoryName != null) {
        categories.putIfAbsent(categoryName, category);
      }
      if (labelName != null) {
        labels.putIfAbsent(labelName, labelValue);
      }
      matches.putIfAbsent(matchId, match);
      agentConfigs.putIfAbsent(agentConfig, agent);
      alerts.putIfAbsent(alertId, alertAggregate);

      return AlertDao
          .builder()
          .analysisId(analysisId)
          .alertId(alertId)
          .matchId(matchId)
          .agentConfigFeatureId(featureConfigId)
          .feature(featureName)
          .agentConfig(agentConfig)
          .policy(policy)
          .strategy(strategy)
          .featureValue(featureValue)
          .featureReason(featureReason)
          .clientMatchId(clientMatchId)
          .category(categoryName)
          .categoryValue(categoryValue)
          .build();
    }
  }
}
