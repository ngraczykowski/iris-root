package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.presentation.dto.workflow.PostApprovalChangesRequest;
import com.silenteight.sens.webapp.backend.presentation.dto.workflow.PostChangeRequest;
import com.silenteight.sens.webapp.backend.presentation.dto.workflow.PostRejectChangeRequest;
import com.silenteight.sens.webapp.backend.support.CsvResponseWriter;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class ChangeRequestRestController {

  private static final String CHANGES_PREFIX = "/changes";
  private static final String AUDIT_CHANGE_REQUEST_FILENAME = "change-request-audit.csv";

  @PostMapping(CHANGES_PREFIX + "/create")
  public ResponseEntity<ProposeChangesResponse> createChanges(
      @Valid @RequestBody PostChangeRequest request) {
    ProposeChangesResponse response = new ProposeChangesResponse();
    return ResponseEntity.ok(response);
  }

  @PostMapping(CHANGES_PREFIX + "/reject")
  public ResponseEntity<Void> rejectChanges(@Valid @RequestBody PostRejectChangeRequest request) {
    return ResponseEntity.noContent().build();
  }

  @PostMapping(CHANGES_PREFIX + "/bulk/approve")
  public ResponseEntity<Void> bulkApproveChanges(
      @Valid @RequestBody PostApprovalChangesRequest request) {
    return ResponseEntity.noContent().build();
  }

  @PostMapping(CHANGES_PREFIX + "/bulk/reject")
  public ResponseEntity<Void> bulkRejectChanges(
      @Valid @RequestBody PostApprovalChangesRequest request) {
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/audit-trail/change-request")
  @PreAuthorize("hasAuthority('AUDIT_GENERATE_REPORT')")
  public void changeRequestAudit(HttpServletResponse response) throws IOException {
    LinesSupplier
        builder =
        new CsvBuilder<>(new ArrayList<ChangeRequestAuditView>().stream());
    CsvResponseWriter csvResponseWriter = new CsvResponseWriter();
    csvResponseWriter.write(response, AUDIT_CHANGE_REQUEST_FILENAME, builder);
  }

  private static class ProposeChangesResponse {

  }

  private static class ChangeRequestAuditView {

  }
}
