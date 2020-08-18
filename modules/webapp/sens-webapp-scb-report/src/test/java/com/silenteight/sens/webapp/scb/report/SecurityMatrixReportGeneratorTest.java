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
        "Permissions (technical),\"Permission / Action",
        "\",Role (business),,,",
        "hasPermission(),,Administrator,Approver,Auditor,Business Operator",
        "APPROVE_CHANGE_REQUESTS,May approve ChangeRequest for any Reasoning Branch in the given"
            + " tree,,x,,",
        "ARCHIVE_DISCREPANCIES,May archive discrapent Reasoning Branches with alerts turned off"
            + " by Circuit Breaker,,,,x",
        "CANCEL_CHANGE_REQUESTS,May cancel aready created ChangeRequest for any Reasoning Branch"
            + " in the given tree,,,,x",
        "CREATE_CHANGE_REQUESTS,May create a ChangeRequest for any Reasoning Branch in the given"
            + " tree,,,,x",
        "GENERATE_REPORTS,Generate all audit reports,x,x,x,x",
        "MANAGE_USERS,\"Managing users",
        "Managing roles assignment",
        "Managing passwords reset for users\",x,,,",
        "REJECT_CHANGE_REQUESTS,May reject ChangeRequest for any Reasoning Branch in the given"
            + " tree,,x,,",
        "SYNC_ANALYSTS,Run analyst synchronization job,x,,,",
        "VALIDATE_REASONING_BRANCHES,Check if given Reasoning Branches exists,,,,x",
        "VIEW_AVAILABLE_USER_ROLES,View user roles,x,,,",
        "VIEW_CHANGE_REQUESTS,View created Change Requests,,x,,x",
        "VIEW_DECISION_TREE,View Decision Tree Details,,x,,x",
        "VIEW_DISCREPANCIES,View Reasoning Branches with alerts turned off by"
            + " Circuit Breaker,,,,x",
        "VIEW_FEATURES,View Reasoning Branch Features names and values,,x,,x",
        "VIEW_REASONING_BRANCHES,View Reasoning Branches with all properties,,x,,x",
        "VIEW_USERS,View users,x,,,"
    };
  }
}
