package com.silenteight.sep.filestorage.minio.remove;

import com.silenteight.sep.filestorage.api.FileRemover;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioFileRemoverConfiguration {

  @Bean
  FileRemover minioFileRemover(MinioClient minioClient) {
    return new MinioFileRemover(minioClient);
  }
}
