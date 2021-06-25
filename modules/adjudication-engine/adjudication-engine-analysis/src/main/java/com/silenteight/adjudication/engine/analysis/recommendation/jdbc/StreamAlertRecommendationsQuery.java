package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.analysis.recommendation.domain.AlertRecommendation;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
class StreamAlertRecommendationsQuery {

  private final JdbcTemplate jdbcTemplate;

  int execute(long analysisId, Consumer<AlertRecommendation> consumer) {
    return doExecute("SELECT *\n"
        + "FROM ae_comments_context acc\n"
        + "WHERE acc.analysis_id = ?", consumer, analysisId);
  }

  int execute(long analysisId, long datasetId, Consumer<AlertRecommendation> consumer) {
    return doExecute("SELECT *\n"
        + "FROM ae_comments_context acc\n"
        + "WHERE acc.analysis_id = ?\n"
        + "  AND acc.alert_id "
        + "IN (\n"
        + "    SELECT alert_id\n"
        + "    FROM ae_dataset_alert\n"
        + "    WHERE dataset_id = ?\n"
        + ")", consumer, analysisId, datasetId);
  }

  private int doExecute(String sql, Consumer<AlertRecommendation> consumer, Object... args) {
    var recommendations = jdbcTemplate.query(
        sql, new AlertRecommendationExtractor(consumer), args);

    return recommendations != null ? recommendations : 0;
  }
}
