package com.silenteight.serp.governance.changerequest.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.CHANGE_REQUEST_ENDPOINT_TAG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = CHANGE_REQUEST_ENDPOINT_TAG)
class ChangeRequestDetailsRestController {

  @NonNull
  private final ChangeRequestDetailsQuery changeRequestQuery;

  @GetMapping(value = "/v1/changeRequests/{id}")
  @PreAuthorize("isAuthorized('GET_CHANGE_REQUEST')")
  public ResponseEntity<ChangeRequestDto> details(@PathVariable UUID id) {
    log.info("Getting details for changeRequest, changeRequestId={}",id);
    return ok(changeRequestQuery.details(id));
  }
}
