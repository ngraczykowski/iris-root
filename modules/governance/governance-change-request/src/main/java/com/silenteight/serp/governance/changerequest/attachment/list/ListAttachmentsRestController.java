package com.silenteight.serp.governance.changerequest.attachment.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
class ListAttachmentsRestController {

  @NonNull
  private final ListAttachmentsQuery listAttachmentsQuery;

  @GetMapping("/v1/changeRequests/{changeRequestId}/attachments")
  @PreAuthorize("isAuthorized('LIST_ATTACHMENTS')")
  public ResponseEntity<List<String>> list(@PathVariable UUID changeRequestId) {
    log.debug("List attachments for changeRequest {} received.", changeRequestId);

    return ok(listAttachmentsQuery.list(changeRequestId));
  }
}
