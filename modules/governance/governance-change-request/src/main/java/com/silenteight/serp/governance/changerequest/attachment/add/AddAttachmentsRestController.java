package com.silenteight.serp.governance.changerequest.attachment.add;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.ATTACHMENTS_REGEXP;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.INVALID_ATTACHMENT_UUID_MSG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@Validated
@RequestMapping(ROOT)
@RequiredArgsConstructor
class AddAttachmentsRestController {

  @NonNull
  private final AddAttachmentsUseCase addAttachmentsUseCase;

  @PostMapping("/v1/changeRequests/{changeRequestId}/attachments")
  @PreAuthorize("isAuthorized('UPLOAD_ATTACHMENTS')")
  public ResponseEntity<Void> addAttachments(
      @PathVariable UUID changeRequestId,
      @RequestBody @Valid List<@Pattern(message = INVALID_ATTACHMENT_UUID_MSG,
              regexp = ATTACHMENTS_REGEXP) String> attachments) {
    log.debug("Add attachments for changeRequest {} received, attachments {}",
        changeRequestId, attachments);

    addAttachmentsUseCase.addAttachments(changeRequestId, attachments);
    return accepted().build();
  }
}
