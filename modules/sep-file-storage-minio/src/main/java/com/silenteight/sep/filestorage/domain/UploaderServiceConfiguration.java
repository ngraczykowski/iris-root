package com.silenteight.sep.filestorage.domain;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploaderServiceConfiguration {

  @Autowired
  MinioClient client;

  @Bean
  MinioFileService uploaderService(MinioClient client) {
    return new MinioFileService(client);
  }
}
