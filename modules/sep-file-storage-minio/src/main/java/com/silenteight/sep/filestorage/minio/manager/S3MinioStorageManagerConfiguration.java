package com.silenteight.sep.filestorage.minio.manager;

import com.silenteight.sep.filestorage.api.StorageManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3MinioStorageManagerConfiguration {

  @Bean
  StorageManager minioStorageManager(S3Client s3Client) {
    return new S3MinioStorageManager(s3Client);
  }
}
