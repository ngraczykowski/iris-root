package com.silenteight.serp.governance.file.storage;

import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.FileUploader;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageConfiguration {

  @Bean
  FileService fileService(
      FileRetriever fileRetriever,
      FileUploader fileUploader,
      FileRemover fileRemover,
      @Valid FileStorageProperties properties) {

    return new FileService(properties.getDefaultBucket(), fileRetriever, fileUploader, fileRemover);
  }
}
