package com.silenteight.sep.filestorage.minio.save;

import com.silenteight.sep.filestorage.api.FileUploader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3MinioFileUploaderConfiguration {

  @Bean
  FileUploader minioFileUploader(S3Client s3Client) {
    return new S3MinioFileUploader(s3Client);
  }
}
