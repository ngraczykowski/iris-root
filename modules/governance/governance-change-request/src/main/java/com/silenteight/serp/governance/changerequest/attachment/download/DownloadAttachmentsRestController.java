package com.silenteight.serp.governance.changerequest.attachment.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.file.storage.FileWrapper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static java.lang.String.format;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class DownloadAttachmentsRestController {

  @NonNull
  private final DownloadAttachmentsUseCase downloadAttachmentsUseCase;

  @GetMapping("/v1/changeRequest/{changeRequestId}/attachments/download")
  @PreAuthorize("isAuthorized('DOWNLOAD_ATTACHMENTS')")
  public ResponseEntity<byte[]> downloadFile(
      @PathVariable String changeRequestId, @RequestParam(name = "file") String file) {

    log.debug("Download attachment for changeRequest {} received, file {}",
              changeRequestId, file);

    FileWrapper fileWrapper = downloadAttachmentsUseCase.activate(file);

    return ResponseEntity.ok()
        .header("Content-Disposition", format("attachment; filename=%s", fileWrapper.getFileName()))
        .header("Content-Type", fileWrapper.getMimeType())
        .body(fileWrapper.getContent());
  }
}
