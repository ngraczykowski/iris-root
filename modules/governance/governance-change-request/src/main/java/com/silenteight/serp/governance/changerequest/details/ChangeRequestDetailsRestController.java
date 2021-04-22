package com.silenteight.serp.governance.changerequest.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.details.dto.ChangeRequestDetailsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ChangeRequestDetailsRestController {

  @NonNull
  private final ChangeRequestDetailsQuery changeRequestQuery;

  @GetMapping(value = "/v1/changeRequests/{id}")
  @PreAuthorize("isAuthorized('GET_CHANGE_REQUEST')")
  public ResponseEntity<ChangeRequestDetailsDto> details(@PathVariable UUID id) {
    return ok(changeRequestQuery.details(id));
  }
}
