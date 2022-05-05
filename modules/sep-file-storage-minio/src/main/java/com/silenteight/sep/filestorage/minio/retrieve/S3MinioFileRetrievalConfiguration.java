package com.silenteight.sep.filestorage.minio.retrieve;

import com.silenteight.sep.filestorage.api.FileRetriever;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3MinioFileRetrievalConfiguration {

  @Bean
  FileRetriever minioFileRetrieval(S3Client s3Client) {
    return new S3MinioFileRetriever(s3Client);
  }
}
