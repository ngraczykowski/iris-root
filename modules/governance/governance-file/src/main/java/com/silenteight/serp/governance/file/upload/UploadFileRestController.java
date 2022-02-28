package com.silenteight.serp.governance.file.upload;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static com.silenteight.serp.governance.file.domain.DomainConstants.FILE_ENDPOINT_TAG;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = FILE_ENDPOINT_TAG)
class UploadFileRestController {

  @NonNull
  private final UploadFileUseCase uploadFileUseCase;

  @PostMapping(value = "/v1/files", consumes = "multipart/form-data")
  @PreAuthorize("isAuthorized('UPLOAD_ATTACHMENTS')")
  public ResponseEntity<FileReferenceDto> upload(
      @RequestParam("file") MultipartFile file, Authentication authentication) {

    return ResponseEntity.ok(uploadFileUseCase.activate(file, authentication.getName()));
  }
}
