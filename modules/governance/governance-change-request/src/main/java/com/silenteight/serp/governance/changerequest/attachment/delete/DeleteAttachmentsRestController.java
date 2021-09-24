package com.silenteight.serp.governance.changerequest.attachment.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class DeleteAttachmentsRestController {

  @NonNull
  private final DeleteAttachmentsUseCase deleteAttachmentsUseCase;

  @DeleteMapping("/v1/changeRequest/{changeRequestId}/attachments")
  @PreAuthorize("isAuthorized('REMOVE_ATTACHMENTS')")
  public ResponseEntity<Void> deleteAttachment(
      @PathVariable UUID changeRequestId,
      @RequestParam(name = "file") String file) {

    log.debug("Delete attachment for changeRequest {} received, attachmentId {}",
              changeRequestId, file);

    deleteAttachmentsUseCase.deleteAttachments(changeRequestId, file);
    return accepted().build();
  }
}
