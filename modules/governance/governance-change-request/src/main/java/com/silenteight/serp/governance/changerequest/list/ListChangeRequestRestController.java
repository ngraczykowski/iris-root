package com.silenteight.serp.governance.changerequest.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestState;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static java.util.Set.of;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ListChangeRequestRestController {

  @NonNull
  private final ListChangeRequestsQuery changeRequestQuery;

  @GetMapping(value = "/v1/changeRequests", params = "state")
  @PreAuthorize("isAuthorized('LIST_CHANGE_REQUESTS')")
  public ResponseEntity<Collection<ChangeRequestDto>> list(
      @RequestParam ChangeRequestState... state) {

    return ok(changeRequestQuery.listByStates(of(state)));
  }
}
