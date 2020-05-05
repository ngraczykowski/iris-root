package com.silenteight.sens.webapp.audit.report;

import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;
import com.silenteight.sens.webapp.report.Report;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

class AuditReportGeneratorTest {

  private AuditReportGenerator underTest = new AuditReportGenerator(
      new MockTimeSource(Instant.parse("2020-04-22T15:15:30Z")),
      new TestDateFormatter());

  @Test
  void generateAuditReportWhenRequested() {
    Report report = underTest.generateReport(emptyMap());

    assertThat(report.getReportFileName()).isEqualTo("audit-2020-04-22 15-15-30.csv");
    assertThat(report.getReportContent().lines()).containsExactly(getExpectedReport());
  }

  @NotNull
  private static String[] getExpectedReport() {
    return new String[] {
        "event_id,parent_event_id,correlation_id,timestamp,type,principal,entity_id,entity_class,"
            + "entity_action,details"
    };
  }
}
