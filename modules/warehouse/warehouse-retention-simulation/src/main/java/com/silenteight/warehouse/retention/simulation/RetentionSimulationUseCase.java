package com.silenteight.warehouse.retention.simulation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.substringAfterLast;

@RequiredArgsConstructor
@Slf4j
public class RetentionSimulationUseCase {

  @Language("PostgreSQL")
  private static final String DROP_SIM_TABLE_QUERY =
      "DROP TABLE IF EXISTS warehouse_simulation_alert_%s";
  private static final String ANALYSIS_SEPARATOR = "/";
  private static final String DASH = "-";
  private static final String UNDERSCORE = "_";

  @NonNull
  private final JdbcTemplate jdbcTemplate;

  @Transactional
  public void activate(List<String> analysisExpired) {
    log.debug("Looking up for {} analysis to remove.", analysisExpired.size());
    analysisExpired.forEach(this::erase);
    log.debug("Removed {} analysis in total.", analysisExpired.size());
  }

  private void erase(String analysis) {
    String analysisId = getAnalysisId(analysis);
    String queryWithAnalysisId = format(DROP_SIM_TABLE_QUERY, analysisId);
    jdbcTemplate.execute(queryWithAnalysisId);
  }

  private static String getAnalysisId(String analysis) {
    return substringAfterLast(analysis, ANALYSIS_SEPARATOR).replace(DASH, UNDERSCORE);
  }
}
