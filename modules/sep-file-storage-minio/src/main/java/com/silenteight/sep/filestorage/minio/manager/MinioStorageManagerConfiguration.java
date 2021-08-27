package com.silenteight.sep.filestorage.minio.manager;

import com.silenteight.sep.filestorage.api.StorageManager;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioStorageManagerConfiguration {

  @Bean
  StorageManager minioStorageManager(MinioClient minioClient) {
    return new MinioStorageManager(minioClient);
  }
}
