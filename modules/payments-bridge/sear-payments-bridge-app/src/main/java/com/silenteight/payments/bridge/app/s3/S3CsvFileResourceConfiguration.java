package com.silenteight.payments.bridge.app.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

@RequiredArgsConstructor
@Configuration
@Slf4j
@EnableConfigurationProperties(S3CsvFileResourceProviderProperties.class)
@Profile("!mockaws")
public class S3CsvFileResourceConfiguration {

  private final S3CsvFileResourceProviderProperties properties;

  @Bean
  S3Client s3Client() {
    log.info("Establish s3 client connection");
    return S3Client.builder()
        .serviceConfiguration(sc -> sc.useArnRegionEnabled(true))
        .build();
  }

  @Bean
  @Primary
  CsvFileResourceProvider awsCsvFileResourceProvider() {
    return new S3CsvFileResourceProvider(
        s3Client(), properties.getBucketName(), properties.getPrefix());
  }

}
