package com.silenteight.warehouse.test.flows.analysisexpired;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AnalysisExpired;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
class MessageGenerator {

  static final String SQL_SELECT_RANDOM_ANALYSIS_NAMES =
      "SELECT * FROM (SELECT DISTINCT analysis_name FROM warehouse_simulation_alert) "
          + "AS wsa order by random() limit %d";

  @NonNull
  private final JdbcTemplate jdbcTemplate;

  AnalysisExpired generateAnalysisExpired(int analysisCount) {
    List<String> analysisNames = fetchRandomAnalysisNames(analysisCount);
    return AnalysisExpired
        .newBuilder()
        .addAllAnalysis(analysisNames)
        .build();
  }

  private List<String> fetchRandomAnalysisNames(int count) {
    List<String> analysisNames = jdbcTemplate.queryForList(
        format(SQL_SELECT_RANDOM_ANALYSIS_NAMES, count),
        String.class);

    if (analysisNames.size() < count) {
      log.warn("Insufficient amount of simulation alerts in the database. "
          + "Generate more simulation alerts first. "
          + "requestCount={}, actualCount={}", count, analysisNames.size());
    }

    if (analysisNames.isEmpty()) {
      throw new IllegalStateException("No simulation alerts in the database");
    }

    return analysisNames;
  }
}
