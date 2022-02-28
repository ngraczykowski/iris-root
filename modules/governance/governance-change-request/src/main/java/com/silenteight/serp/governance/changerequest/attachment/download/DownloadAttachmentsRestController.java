package com.silenteight.serp.governance.changerequest.attachment.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.file.storage.FileWrapper;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.ATTACHMENTS_REGEXP;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.CHANGE_REQUEST_ENDPOINT_TAG;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.INVALID_ATTACHMENT_UUID_MSG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.BAD_REQUEST_DESCRIPTION;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.BAD_REQUEST_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.OK_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static java.lang.String.format;

@Slf4j
@Validated
@RestController
@RequestMapping(value = ROOT)
@RequiredArgsConstructor
@Tag(name = CHANGE_REQUEST_ENDPOINT_TAG)
class DownloadAttachmentsRestController {

  @NonNull
  private final DownloadAttachmentsUseCase downloadAttachmentsUseCase;

  @GetMapping("/v1/changeRequests/{changeRequestId}/attachments/download")
  @PreAuthorize("isAuthorized('DOWNLOAD_ATTACHMENTS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION,
          content = @Content(schema = @Schema(implementation = File.class))),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION,
          content = @Content)
  })
  public ResponseEntity<byte[]> downloadFile(
      @PathVariable String changeRequestId,
      @RequestParam(name = "file") @Valid @Pattern(message = INVALID_ATTACHMENT_UUID_MSG,
          regexp = ATTACHMENTS_REGEXP) String file) {

    log.debug("Download attachment for changeRequest {} received, file {}",
        changeRequestId, file);

    FileWrapper fileWrapper = downloadAttachmentsUseCase.activate(file);

    return ResponseEntity.ok()
        .header("Content-Disposition", format("attachment; filename=%s", fileWrapper.getFileName()))
        .header("Content-Type", fileWrapper.getMimeType())
        .body(fileWrapper.getContent());
  }
}
