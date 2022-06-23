package com.silenteight.serp.governance.changerequest.attachment.download;

import com.silenteight.serp.governance.file.description.FileDescriptionQuery;
import com.silenteight.serp.governance.file.storage.FileService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloadAttachmentsConfiguration {

  @Bean
  DownloadAttachmentsUseCase downloadAttachmentsUseCase(
      FileDescriptionQuery fileDescriptionQuery,
      FileService fileService) {
    return new DownloadAttachmentsUseCase(fileDescriptionQuery, fileService);
  }
}
