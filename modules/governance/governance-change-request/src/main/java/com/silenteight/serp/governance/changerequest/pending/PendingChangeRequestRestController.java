package com.silenteight.serp.governance.changerequest.pending;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.pending.dto.PendingChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class PendingChangeRequestRestController {

  @NonNull
  private final PendingChangeRequestQuery changeRequestQuery;

  @GetMapping(value = "/v1/changeRequests", params = "state=PENDING")
  @PreAuthorize("isAuthorized('LIST_PENDING_CHANGE_REQUESTS')")
  public ResponseEntity<List<PendingChangeRequestDto>> pending() {
    return ok(changeRequestQuery.listPending());
  }
}
