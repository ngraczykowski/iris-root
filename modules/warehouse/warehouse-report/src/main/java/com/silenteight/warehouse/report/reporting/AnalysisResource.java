package com.silenteight.warehouse.report.reporting;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class AnalysisResource {

  static final String ANALYSIS_PREFIX = "analysis/";

  public static String toResourceName(String analysisName) {
    return ANALYSIS_PREFIX + analysisName;
  }
}
