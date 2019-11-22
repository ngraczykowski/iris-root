package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.DecisionTreeAuditDto;
import com.silenteight.sens.webapp.backend.presentation.dto.model.AiModelAuditDto;
import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.users.user.dto.UserAuditDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('AUDIT_GENERATE_REPORT')")
public class AuditTrailController {

  private static final String AUDIT_MODEL_FILENAME = "model-audit.csv";
  private static final String AUDIT_DECISION_TREE_FILENAME = "decision-tree-audit.csv";
  private static final String AUDIT_REASONING_BRANCH_FILENAME = "reasoning-branch-audit.csv";
  private static final String CIRCUIT_BREAKER_TRIGGERED_ALERTS =
      "circuit_breaker_triggered_alerts.csv";
  private static final String AUDIT_USER_FILENAME = "user-audit.csv";

  private static final String AI_MODEL_ID_HEADER = "ai_model_id";
  private static final String AI_MODEL_NAME_HEADER = "ai_model_name";
  private static final String AUDITED_OPERATION_HEADER = "audited_operation";
  private static final String AUDITED_AT_HEADER = "audited_at";
  private static final String DECISION_TREE_ID_HEADER = "decision_tree_id";
  private static final String DECISION_TREE_NAME_HEADER = "decision_tree_name";
  private static final String ASSIGNED_BATCH_TYPES_HEADER = "assigned_batch_types";
  private static final String ACTIVATED_BATCH_TYPES_HEADER = "activated_batch_types";
  private static final String MODIFIED_BY_HEADER = "modified_by";

  private final CsvResponseWriter csvResponseWriter = new CsvResponseWriter();

  @GetMapping("/audit-trail/model")
  public void modelAudit(HttpServletResponse response) throws IOException {
    // TODO move csv building to service
    CsvBuilder<AiModelAuditDto> builder =
        new CsvBuilder<>(new ArrayList<AiModelAuditDto>().stream());

    csvResponseWriter.write(response, AUDIT_MODEL_FILENAME, builder);
  }

  @GetMapping("/audit-trail/decision-tree")
  public void decisionTreeAudit(HttpServletResponse response) throws IOException {
    // TODO move csv building to service
    CsvBuilder<DecisionTreeAuditDto> builder =
        new CsvBuilder<>(new ArrayList<DecisionTreeAuditDto>().stream());

    csvResponseWriter.write(response, AUDIT_DECISION_TREE_FILENAME, builder);
  }

  @GetMapping("/audit-trail/reasoning-branch")
  public void reasoningBranchAudit(HttpServletResponse response) throws IOException {
    CsvBuilder<ReasoningBranchAuditDto> builder =
        new CsvBuilder<>(new ArrayList<ReasoningBranchAuditDto>().stream());

    csvResponseWriter.write(response, AUDIT_REASONING_BRANCH_FILENAME, builder);
  }

  @GetMapping("/audit-trail/user")
  public void userAudit(HttpServletResponse response) throws IOException {
    CsvBuilder<UserAuditDto> builder =
        new CsvBuilder<>(new ArrayList<UserAuditDto>().stream());

    csvResponseWriter.write(response, AUDIT_USER_FILENAME, builder);
  }

  @GetMapping("/audit-trail/circuit-breaker-triggered-alerts")
  public void wronglySolvedBranchAudit(HttpServletResponse response) throws IOException {
    CsvBuilder<AlertsFromWronglySolvedBranchesAuditDto> builder =
        new CsvBuilder<>(new ArrayList<AlertsFromWronglySolvedBranchesAuditDto>().stream());

    csvResponseWriter.write(response, CIRCUIT_BREAKER_TRIGGERED_ALERTS, builder);
  }

  private static class ReasoningBranchAuditDto {

  }

  private static class AlertsFromWronglySolvedBranchesAuditDto {

  }
}
