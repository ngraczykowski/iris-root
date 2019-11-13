package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class ApprovalQueueRestController {

  private static final String APPROVAL_QUEUE_PREFIX = "/approval-queue";

  @GetMapping(APPROVAL_QUEUE_PREFIX)
  public ResponseEntity<ApprovalQueueView> getApprovalQueue() {
    return ResponseEntity.ok(new ApprovalQueueView());
  }

  private static class ApprovalQueueView {

  }
}
