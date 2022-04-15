package com.silenteight.serp.governance.changerequest.attachment.delete;

import com.silenteight.serp.governance.changerequest.attachment.domain.ChangeRequestAttachmentsService;
import com.silenteight.serp.governance.file.domain.FileReferenceService;
import com.silenteight.serp.governance.file.storage.FileService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteAttachmentsConfiguration {

  @Bean
  DeleteAttachmentsUseCase deleteAttachmentsUseCase(
      FileService fileService,
      ChangeRequestAttachmentsService changeRequestAttachmentsService,
      FileReferenceService fileReferenceService) {

    return new DeleteAttachmentsUseCase(fileService, changeRequestAttachmentsService,
        fileReferenceService);
  }
}
