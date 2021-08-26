package com.silenteight.sep.filestorage.minio;

import com.silenteight.sep.filestorage.minio.manager.MinioStorageManager;
import com.silenteight.sep.filestorage.minio.remove.MinioFileRemover;
import com.silenteight.sep.filestorage.minio.retrieval.MinioFileRetrieval;
import com.silenteight.sep.filestorage.minio.save.MinioFileUploader;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioFileStorageConfiguration {

  @Bean
  MinioStorageManager minioStorageManager(MinioClient minioClient) {
    return new MinioStorageManager(minioClient);
  }

  @Bean
  MinioFileRemover minioFileRemover(MinioClient minioClient) {
    return new MinioFileRemover(minioClient);
  }

  @Bean
  MinioFileRetrieval minioFileRetrieval(MinioClient minioClient) {
    return new MinioFileRetrieval(minioClient);
  }

  @Bean
  MinioFileUploader minioFileUploader(MinioClient minioClient) {
    return new MinioFileUploader(minioClient);
  }

  @Bean
  MinioFileService minioFileService(
      MinioFileUploader minioFileUploader,
      MinioFileRetrieval minioFileRetrieval,
      MinioFileRemover minioFileRemover,
      MinioStorageManager minioStorageManager) {

    return new MinioFileService(minioFileUploader, minioFileRetrieval, minioFileRemover,
        minioStorageManager);
  }

}
