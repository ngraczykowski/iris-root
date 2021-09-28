package com.silenteight.warehouse.report.storage;

import com.silenteight.sep.filestorage.api.FileRemover;
import com.silenteight.sep.filestorage.api.FileRetriever;
import com.silenteight.sep.filestorage.api.FileUploader;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ReportStorageProperties.class)
class ReportStorageConfiguration {

  @Bean
  MinioReportStorageService reportStorageService(
      FileUploader fileUploader,
      FileRemover fileRemover,
      FileRetriever fileRetriever,
      @Valid ReportStorageProperties properties) {

    return new MinioReportStorageService(
        fileUploader, fileRemover, fileRetriever, properties.getDefaultBucket());
  }
}
