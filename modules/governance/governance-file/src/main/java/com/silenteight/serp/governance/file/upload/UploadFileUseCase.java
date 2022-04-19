package com.silenteight.serp.governance.file.upload;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.domain.dto.FileReferenceDto;
import com.silenteight.serp.governance.file.storage.FileService;
import com.silenteight.serp.governance.file.validation.ValidationService;

import org.springframework.web.multipart.MultipartFile;

import static com.silenteight.serp.governance.file.common.FileResource.fromResourceName;
import static com.silenteight.serp.governance.file.common.MimeDetector.getMimeType;

@Slf4j
@RequiredArgsConstructor
public class UploadFileUseCase {

  @NonNull
  private final ValidationService validationService;

  @NonNull
  private final FileService fileService;

  @NonNull
  private final FileReferenceService fileReferenceService;

  public FileReferenceDto activate(MultipartFile file, String uploaderName) {
    log.debug("Upload request for file {} received.", file.getOriginalFilename());
    validationService.validate(file);

    String fileName = fileService.attemptToSaveFile(file);

    FileReferenceDto fileReferenceDto = fileReferenceService.saveFileReference(
        (fromResourceName(fileName)),
        uploaderName,
        file.getOriginalFilename(),
        file.getSize(),
        getMimeType(file));

    log.debug("Upload request for file {} processed.", file.getOriginalFilename());
    return fileReferenceDto;
  }
}
