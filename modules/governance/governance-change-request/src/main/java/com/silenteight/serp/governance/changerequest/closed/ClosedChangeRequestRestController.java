package com.silenteight.serp.governance.changerequest.closed;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.closed.dto.ClosedChangeRequestDto;

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
class ClosedChangeRequestRestController {

  @NonNull
  private final ClosedChangeRequestQuery changeRequestQuery;

  @GetMapping(value = "/v1/changeRequests", params = "state=CLOSED")
  @PreAuthorize("isAuthorized('LIST_CLOSED_CHANGE_REQUESTS')")
  public ResponseEntity<List<ClosedChangeRequestDto>> closed() {
    return ok(changeRequestQuery.listClosed());
  }
}
