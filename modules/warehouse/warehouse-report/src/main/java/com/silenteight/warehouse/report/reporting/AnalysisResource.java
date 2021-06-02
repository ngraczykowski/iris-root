package com.silenteight.warehouse.report.reporting;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class AnalysisResource {

  public static final String ANALYSIS_PREFIX = "analysis/";

  public static String toResourceName(String analysisName) {
    return ANALYSIS_PREFIX + analysisName;
  }
}
