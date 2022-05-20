package com.silenteight.sep.filestorage.minio.remove;

import com.silenteight.sep.filestorage.api.FileRemover;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class MinioFileRemoverConfiguration {

  @Bean
  FileRemover minioFileRemover(S3Client s3Client) {
    return new S3MinioFileRemover(s3Client);
  }
}
