package com.silenteight.hsbc.bridge.aws;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsAdapterProperties.class)
@Profile("!dev")
class AwsConfiguration {

  private final AwsAdapterProperties properties;

  @Bean
  AwsAdapter awsAdapter() {
    var buckets = properties.getBuckets();
    return new AwsAdapter(
        createS3Client(),
        buckets.getModelBucketName(),
        buckets.getWatchlistBucketName());
  }

  private S3Client createS3Client() {
    return S3Client.builder()
        .endpointOverride(properties.getUri())
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())))
        .region(Region.of(properties.getRegion()))
        .build();
  }
}
