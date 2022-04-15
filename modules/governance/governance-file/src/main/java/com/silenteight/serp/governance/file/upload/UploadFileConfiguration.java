package com.silenteight.serp.governance.file.upload;

import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.storage.FileService;
import com.silenteight.serp.governance.file.validation.ValidationService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UploadFileConfiguration {

  @Bean
  UploadFileUseCase uploadAttachmentUseCase(
      ValidationService validationService,
      FileService fileService,
      FileReferenceService fileReferenceService) {

    return new UploadFileUseCase(validationService, fileService, fileReferenceService);
  }
}
