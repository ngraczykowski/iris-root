package com.silenteight.sep.filestorage.minio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(S3MinioClientProperties.class)
class S3MinioClientConfiguration {

  @Autowired
  S3MinioClientProperties properties;

  @Bean
  S3Client s3Client() {
    final AwsBasicCredentials credentials =
        AwsBasicCredentials.create(
            properties.getAccessKey(),
            properties.getPrivateKey());

    return S3Client.builder()
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .region(Region.of(properties.getRegion()))
        .endpointOverride(URI.create(properties.getUrl()))
        .serviceConfiguration(buildServiceConfiguration())
        .overrideConfiguration(buildOverrideConfiguration())
        .build();
  }

  private S3Configuration buildServiceConfiguration() {
    return S3Configuration.builder()
        .pathStyleAccessEnabled(true)
        .build();
  }

  private ClientOverrideConfiguration buildOverrideConfiguration() {
    return ClientOverrideConfiguration
        .builder()
        .putAdvancedOption(SdkAdvancedClientOption.SIGNER, AwsS3V4Signer.create())
        .build();
  }
}
