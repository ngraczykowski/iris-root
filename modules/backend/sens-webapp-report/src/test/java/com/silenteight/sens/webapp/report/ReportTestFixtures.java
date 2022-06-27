package com.silenteight.sens.webapp.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ReportTestFixtures {

  static final String REPORT_NAME = "dummy_report_name";

  static final List<String> REPORT_CONTENT = List.of("line1", "line2");
}
