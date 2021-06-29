package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;

import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Slf4j
class GetAlertRecommendationQuery {

  private final JdbcTemplate jdbcTemplate;

  AlertRecommendation execute(long recommendationId) {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM ae_alert_recommendation_query WHERE recommendation_id = ?",
        AlertRecommendationMapper.INSTANCE, recommendationId);
  }
}
