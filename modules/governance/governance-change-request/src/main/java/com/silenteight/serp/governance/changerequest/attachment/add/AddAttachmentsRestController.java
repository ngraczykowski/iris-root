package com.silenteight.serp.governance.changerequest.attachment.add;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.ATTACHMENTS_REGEXP;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.CHANGE_REQUEST_ENDPOINT_TAG;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.INVALID_ATTACHMENT_UUID_MSG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@Validated
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = CHANGE_REQUEST_ENDPOINT_TAG)
class AddAttachmentsRestController {

  @NonNull
  private final AddAttachmentsUseCase addAttachmentsUseCase;

  @PostMapping("/v1/changeRequests/{changeRequestId}/attachments")
  @PreAuthorize("isAuthorized('UPLOAD_ATTACHMENTS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> addAttachments(
      @PathVariable UUID changeRequestId,
      @RequestBody @Valid List<@Pattern(message = INVALID_ATTACHMENT_UUID_MSG,
              regexp = ATTACHMENTS_REGEXP) String> attachments) {

    log.info("Add attachments request for changeRequestId={} received, attachments={}",
        changeRequestId, attachments);

    addAttachmentsUseCase.addAttachments(changeRequestId, attachments);
    return accepted().build();
  }
}
