package com.silenteight.adjudication.engine.analysis.matchsolution.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.aspects.metrics.Timed;

import com.google.common.base.Splitter;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
class AnalyzeLargeTablesQuery {

  @Language("PostgreSQL")
  static final String ANALYZE_SQL =
      "ANALYZE ae_agent_exchange;\n"
          + "ANALYZE ae_agent_exchange_feature;\n"
          + "ANALYZE ae_agent_exchange_match_feature;\n"
          + "ANALYZE ae_alert;\n"
          + "ANALYZE ae_alert_comment_input;\n"
          + "ANALYZE ae_analysis_alert;\n"
          + "ANALYZE ae_analysis_category;\n"
          + "ANALYZE ae_analysis_feature;\n"
          + "ANALYZE ae_dataset_alert;\n"
          + "ANALYZE ae_match;\n"
          + "ANALYZE ae_match_category_value;\n"
          + "ANALYZE ae_match_feature_value;\n"
          + "ANALYZE ae_match_solution;\n"
          + "ANALYZE ae_pending_recommendation;\n"
          + "ANALYZE ae_recommendation";

  private final JdbcTemplate jdbcTemplate;

  @Transactional
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  void execute() {
    Splitter
        .on(';')
        .split(ANALYZE_SQL)
        .forEach(statement -> {
          var sql = statement.trim();

          if (log.isDebugEnabled()) {
            log.debug("Executing: sql=[{}]", sql);
          }

          jdbcTemplate.execute(sql);
        });
  }
}
