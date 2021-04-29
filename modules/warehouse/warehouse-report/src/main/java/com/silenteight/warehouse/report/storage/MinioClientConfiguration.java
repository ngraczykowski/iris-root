package com.silenteight.warehouse.report.storage;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioClientProperties.class)
class MinioClientConfiguration {

  @Autowired
  MinioClientProperties properties;

  @Bean
  MinioClient minioClient() {
    return new MinioClient.Builder()
        .endpoint(properties.getUrl())
        .credentials(properties.getAccessKey(), properties.getPrivateKey())
        .build();
  }
}
