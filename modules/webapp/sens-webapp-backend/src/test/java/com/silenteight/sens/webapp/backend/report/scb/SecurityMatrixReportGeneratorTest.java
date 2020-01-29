package com.silenteight.sens.webapp.backend.report.scb;

import com.silenteight.sens.webapp.backend.report.Report;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SecurityMatrixReportGeneratorTest {

  private SecurityMatrixReportGenerator underTest = new SecurityMatrixReportGenerator();

  @Test
  void generateEntitlementReportWhenRequested() {
    Report report = underTest.generateReport();

    assertThat(report.getReportFileName()).isEqualTo("security-matrix-report.csv");
    assertThat(report.getReportContent().lines()).containsExactly(getExpectedReport());
  }

  @NotNull
  private static String[] getExpectedReport() {
    return new String[] {
        "ACL,ACL Class,Permissions (technical),Authorities (technical),\"Permission / Action",
        "\",Role (business),,,,,,,,",
        ",,hasPermission(),hasAuthority(),,Maker,Approver,Decision Tree Viewer,Decision Tree"
            + " Manager,Batch Type Manager,Auditor,Inbox Operator,User Manager,Analyst",
        "T,Decision Tree,DECISION_TREE_CHANGE,,May create a ChangeRequest for any Reasoning"
            + " Branch in the given tree,X,,,,,,,,",
        "T,Decision Tree,DECISION_TREE_VIEW,,\"May view a decision tree contents"
            + " (RBs, alerts)\",X,X,X,,,,,,",
        "T,Workflow Level,CHANGE_ACCEPT,,Approve ChangeRequest for a given Approval"
            + " Level,,X,,,,,,,",
        "F,,,DECISION_TREE_LIST,May access a decision tree list and open any decision tree"
            + " without its contents (no RB nor alerts),X,X,X,X,X,,,,",
        "F,,,DECISION_TREE_MANAGE,Create empty tree,,,,X,,,,,",
        ",,,,\"\"\"Copy Tree\"\" - without history (show appropriate information)\",,,,,,,,,",
        ",,,,\"\"\"Rename This Tree\"\"\",,,,,,,,,",
        ",,,,\"\"\"Delete This Tree\"\"\",,,,,,,,,",
        "F,,,BATCH_TYPE_MANAGE,\"Assignment of Batch Types to Decision Tree",
        "\",,,,,X,,,,",
        ",,,,Activation of Batch Types for a Decision Tree,,,,,,,,,",
        "F,,,AUDIT_GENERATE_REPORT,Generate all audit reports,,,,,,X,,,",
        "F,,,INBOX_MANAGE,Reading/marking as solved all inbox entries for all Decision"
            + " Trees,,,,,,,X,,",
        "F,,,WORKFLOW_MANAGE,May configure workflow - change approval levels for a given"
            + " tree,,,,,,,,X,",
        "F,,,USER_MANAGE,\"Managing users",
        "Managing roles assignment",
        "Managing permissions assignment\",,,,,,,,X,",
        "F,,,USER_VIEW,View users,,,,,,,,X,",
        "F,,,SOLUTION_VIEW,Reading recommendations through Chrome Extension for all"
            + " Batch Types,,,,,,,,,X",
        ",,,DECISION_TREE_VIEW_ALL,\"May view contents of all decision trees"
            + " (RBs, alerts)\",,,X?,,,,,,"};
  }
}
