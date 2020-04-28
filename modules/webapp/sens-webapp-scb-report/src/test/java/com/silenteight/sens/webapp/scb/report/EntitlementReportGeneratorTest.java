package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.common.testing.time.MockTimeSource;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

class EntitlementReportGeneratorTest {

  private EntitlementReportGenerator underTest = new EntitlementReportGenerator(
      new MockTimeSource(Instant.parse("2011-12-03T10:15:30Z")),
      new ScbReportDateFormatter());

  @Test
  void generateEntitlementReportWhenRequested() {
    Report report = underTest.generateReport(emptyMap());

    assertThat(report.getReportFileName()).isEqualTo("entitlement-report.csv");
    assertThat(report.getReportContent().lines()).containsExactly(getExpectedReport());
  }

  @NotNull
  private static String[] getExpectedReport() {
    return new String[] {
        "Application Name,Surveillance Optimization,,",
        "Report Generated By,System,,",
        "Run Date,20111203101530,,",
        "Number of data records,4,,",
        "Entitlement Name,Entitlement Type,Entitlement Description,Entitlement Link",
        "ROLE_ANALYST,USER,A user that can use a SENS Chrome Extension,",
        "ROLE_AUDITOR,USER,A user that can generate and download reports from UI and CLI,",
        "ROLE_BUSINESS_OPERATOR,USER,"
            + "A user that can manage users and submit changes to reasoning branches,",
        "ADMIN,ADMIN,A user that have all roles mentioned above,"
    };
  }
}
