package com.silenteight.warehouse.report.storage;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(ReportStorageProperties.class)
class ReportStorageConfiguration {

  @Autowired
  @Valid
  ReportStorageProperties properties;

  @Autowired
  MinioClient client;

  @Bean
  ReportStorageService reportStorageService(
      MinioClient client, @Valid ReportStorageProperties properties) {
    return new ReportStorageService(client, properties.getDefaultBucket());
  }
}
