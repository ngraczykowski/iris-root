package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.presentation.dto.workflow.PostWorkflowRequest;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('WORKFLOW_MANAGE')")
public class WorkflowRestController {

  public static final String WORKFLOWS_PREFIX = "/workflows";
  public static final String WORKFLOW_PREFIX = "/workflow";

  @GetMapping(WORKFLOWS_PREFIX)
  public ResponseEntity<WorkflowListView> getWorkflow() {
    return ResponseEntity.ok(new WorkflowListView());
  }

  @GetMapping(WORKFLOW_PREFIX + "/{decisionTreeId}")
  public ResponseEntity<WorkflowDetailsView> getWorkflowDetails(@PathVariable long decisionTreeId) {
    return ResponseEntity.ok(new WorkflowDetailsView());
  }

  @PostMapping(WORKFLOW_PREFIX + "/{decisionTreeId}")
  public ResponseEntity<Void> configureWorkflow(
      @PathVariable long decisionTreeId,
      @Valid @RequestBody PostWorkflowRequest request) {

    return ResponseEntity
        .noContent()
        .location(URI.create(RestConstants.ROOT + WORKFLOW_PREFIX + "/" + decisionTreeId))
        .build();
  }

  private static class WorkflowListView {

  }

  private static class WorkflowDetailsView {

  }
}
