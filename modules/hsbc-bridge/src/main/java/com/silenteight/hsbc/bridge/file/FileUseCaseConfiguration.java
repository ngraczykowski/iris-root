package com.silenteight.hsbc.bridge.file;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class FileUseCaseConfiguration {

  @Bean
  SaveFileUseCase fileTransferUseCase(ResourceSaver resourceSaver) {
    return new SaveFileUseCase(resourceSaver);
  }
}
