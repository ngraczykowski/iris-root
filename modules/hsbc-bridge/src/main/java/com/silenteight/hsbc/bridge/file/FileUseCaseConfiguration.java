package com.silenteight.hsbc.bridge.file;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({ FileProperties.class })
class FileUseCaseConfiguration {

  private final FileProperties fileProperties;

  @Bean
  FileTransferUseCase fileTransferUseCase() {
    return new FileTransferUseCase(fileProperties.getDirectory());
  }
}
