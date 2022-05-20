package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Slf4j
class GetAlertRecommendationQuery {

  private final JdbcTemplate jdbcTemplate;

  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  AlertRecommendation execute(long recommendationId) {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM ae_alert_recommendation_query WHERE recommendation_id = ?",
        AlertRecommendationMapper.INSTANCE, recommendationId);
  }
}
