package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sens.webapp.report.Report;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

class SecurityMatrixReportGeneratorTest {

  private SecurityMatrixReportGenerator underTest = new SecurityMatrixReportGenerator();

  @Test
  void generateEntitlementReportWhenRequested() {
    Report report = underTest.generateReport(emptyMap());

    assertThat(report.getReportFileName()).isEqualTo("security-matrix-report.csv");
    assertThat(report.getReportContent().lines()).containsExactly(getExpectedReport());
  }

  @NotNull
  private static String[] getExpectedReport() {
    return new String[] {
        "Permission name,Permission description,User Administrator,Model Tuner,Approver,Auditor,QA",
        ",,Manages the users in the system,Creates and runs simulations to increase"
            + " effectiveness and efficiencies in the model,Will review and "
            + "approve changes to the model,Audit/Assuarance/IMR and "
            + "internal reviewer access used for read only purposes,"
            + "Will Quality Assure the False Positive Outcomes from"
            + " the S8 Solution and ensure they adhere to expected"
            + " risk appetite for adjudication.",
        "DOWNLOAD_AUDIT_REPORTS,generate Audit Trail report,X,,,X,",
        "DOWNLOAD_USERS_REPORTS,generate a user list report,X,,,X,",
        "MANAGE_USERS,add/modify/remove users,X,,,,",
        "VIEW_USERS,view the list of users,X,,,,",
        "REPORTING_UI (not part of MVP),can open ReportingUI service (Kibana),,X,X,X,X",
        "SYNC_ANALYSTS,,X,,,,",
    };
  }
}
