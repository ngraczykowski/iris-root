package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import static java.util.stream.Stream.empty;

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
    LinesSupplier linesSupplier = new CsvBuilder<>(empty());

    csvResponseWriter.write(response, AUDIT_MODEL_FILENAME, linesSupplier);
  }

  @GetMapping("/audit-trail/decision-tree")
  public void decisionTreeAudit(HttpServletResponse response) throws IOException {
    // TODO move csv building to service
    LinesSupplier linesSupplier = new CsvBuilder<>(empty());

    csvResponseWriter.write(response, AUDIT_DECISION_TREE_FILENAME, linesSupplier);
  }

  @GetMapping("/audit-trail/reasoning-branch")
  public void reasoningBranchAudit(HttpServletResponse response) throws IOException {
    LinesSupplier linesSupplier = new CsvBuilder<>(empty());

    csvResponseWriter.write(response, AUDIT_REASONING_BRANCH_FILENAME, linesSupplier);
  }

  @GetMapping("/audit-trail/user")
  public void userAudit(HttpServletResponse response) throws IOException {
    LinesSupplier linesSupplier = new CsvBuilder<>(empty());

    csvResponseWriter.write(response, AUDIT_USER_FILENAME, linesSupplier);
  }

  @GetMapping("/audit-trail/circuit-breaker-triggered-alerts")
  public void wronglySolvedBranchAudit(HttpServletResponse response) throws IOException {
    LinesSupplier linesSupplier = new CsvBuilder<>(empty());

    csvResponseWriter.write(response, CIRCUIT_BREAKER_TRIGGERED_ALERTS, linesSupplier);
  }
}
