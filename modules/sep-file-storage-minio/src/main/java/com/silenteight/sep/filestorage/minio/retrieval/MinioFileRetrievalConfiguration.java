package com.silenteight.sep.filestorage.minio.retrieval;

import com.silenteight.sep.filestorage.api.FileRetriever;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioFileRetrievalConfiguration {

  @Bean
  FileRetriever minioFileRetrieval(MinioClient minioClient) {
    return new MinioFileRetriever(minioClient);
  }
}
