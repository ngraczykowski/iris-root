package com.silenteight.sens.webapp.backend.changerequest.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.backend.changerequest.rest.dto.ChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.CHANGE_REQUEST;
import static com.silenteight.sens.webapp.backend.security.Authority.APPROVER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ChangeRequestRestController {

  @NonNull
  private final ChangeRequestQuery changeRequestQuery;

  @NonNull
  private final AuditLog auditLog;

  @GetMapping("/change-requests")
  @PreAuthorize(APPROVER)
  public ResponseEntity<List<ChangeRequestDto>> pending() {
    auditLog.logInfo(CHANGE_REQUEST, "Listing pending Change Requests");

    List<ChangeRequestDto> changeRequests = changeRequestQuery.pending();

    auditLog.logInfo(CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());
    return ok(changeRequests);
  }
}
