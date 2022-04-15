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
        "Permission name,Permission description,User Administrator,Model Tuner,"
            + "Approver,Auditor,QA,QA Issue Manager",
        ",,Manages the users in the system,"
            + "Creates and runs simulations to increase performance of the solving model,"
            + "Reviews and approves changes to the model,"
            + "Makes audits of system changes (read only permissions),"
            + "Performs quality checks of S8 recommendations to ensure they "
            + "adhere to expected risk appetite for adjudication,"
            + "Reviews and takes action on all error cases identified by QA analyst",
        "MANAGE_POLICY,create/edit(draft)/clone(any->draft)/delete(draft)/"
            + "archive(ready) policy,,X,,,,X",
        "VIEW_POLICIES,view the list of policies,,X,X,X,X,X",
        "IMPORT_POLICY,import policy from file,,x,,,,",
        "MANAGE_STEPS,create/edit/delete steps in policy (draft state only),,X,,,,X",
        "MANAGE_SIMULATION,create/run simulation,,X,,,,",
        "VIEW_SIMULATIONS,view the list of simulations,,X,X,X,,",
        "VIEW_SIMULATION_RESULT,view the result of the simulation,,X,X,,,",
        "DOWNLOAD_SIMULATION_REPORTS,download reports related to the simulation,,X,X,,,",
        "DOWNLOAD_SOLVING_REPORTS,\"download reports related to solving in Reports section "
            + "(eg. RB Scorer, AI Reasoning, Accuracy report)\",,X,X,,,",
        "DOWNLOAD_AUDIT_REPORTS,generate Audit Trail report (currently not available in "
            + "UI for HSBC),X,,,X,,",
        "DOWNLOAD_USERS_REPORTS,generate a user list report (currently not available in "
            + "UI for HSBC),X,,,X,,",
        "DOWNLOAD_PERIODIC_REPORTS,download reports generated automatically in fixed "
            + "period of times from production data,,,,,,",
        "CREATE_DATASET,create datasets (grouping alerts),,X,,,,",
        "VIEW_DATASETS,view the list of datasets,,X,X,X,,",
        "PROMOTE_MODEL_AS_CHANGE_REQUEST,can crate a change request to promote model on "
            + "a production,,X,,,,",
        "VIEW_APPROVAL_QUEUE,view the list of change reqeusts in approval queue,,X,X,X,,",
        "CANCEL_CHANGE_REQUEST -> CANCEL_APPROVAL_TICKET,can cancel change request created "
            + "by himself,,X,,,,",
        "APPROVE_CHANGE_REQUEST -> APPROVE_APPROVAL_TICKET,can approve change request "
            + "(promode model on a production),,,X,,,",
        "REJECT_CHANGE_REQUEST -> REJECT_APPROVAL_TICKET,can reject change request,,,X,,,",
        "VIEW_QA_ALERTS,view the list of alerts selected to the QA process,,,,X,X,",
        "MANAGE_QA_ALERT,can close case or rise a QA Exception,,,,,X,",
        "VIEW_QA_VERIFICATION,view the list of failed cases occured in QA process,,,,X,X,X",
        "MANAGE_QA_VERIFICATION,can accept or reject failed cases by introducing changes "
            + "to policy or/and agents,,,,,,X",
        "DASHBOARD,can view the Dashboard,,X,X,X,X,X",
        "VIEW_AI_SUGGESTIONS,view the list of AI suggested changes,,X,,X,,",
        "MANAGE_AI_SUGGESTION,can reject suggested changes or promote it to the production,,X,,,,",
        "MANAGE_USERS,add/modify/remove users,X,,,,,",
        "VIEW_USERS,view the list of users,X,,,,,",
        "REPORTING_UI (not part of MVP),can open ReportingUI service (Kibana),,X,X,X,X,",
        "VIEW_REASONING_BRANCHES,can open and view section of Reasoning Branches,,X,X,X,,",
    };
  }
}
