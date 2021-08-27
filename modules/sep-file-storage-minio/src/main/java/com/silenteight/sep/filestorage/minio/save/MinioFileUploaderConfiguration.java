package com.silenteight.sep.filestorage.minio.save;

import com.silenteight.sep.filestorage.api.FileUploader;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioFileUploaderConfiguration {

  @Bean
  FileUploader minioFileUploader(MinioClient minioClient) {
    return new MinioFileUploader(minioClient);
  }
}
