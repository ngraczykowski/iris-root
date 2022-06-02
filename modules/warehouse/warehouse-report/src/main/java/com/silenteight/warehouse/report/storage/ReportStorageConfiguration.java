package com.silenteight.warehouse.report.storage;

import com.silenteight.warehouse.report.persistence.ReportPersistenceService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Optionals;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;
import java.util.Optional;
import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties({
    ReportStorageProperties.class, ReportStorageClientProperties.class })
class ReportStorageConfiguration {

  @Bean
  S3Client s3Client(ReportStorageClientProperties reportStorageClientProperties) {
    final AwsCredentialsProvider provider =
        Optionals.mapIfAllPresent(
                Optional.ofNullable(reportStorageClientProperties.getAccessKey()),
                Optional.ofNullable(reportStorageClientProperties.getPrivateKey()),
                (accessKey, privateKey) ->
                    AwsBasicCredentials.create(
                        reportStorageClientProperties.getAccessKey(),
                        reportStorageClientProperties.getPrivateKey()))
            .<AwsCredentialsProvider>map(StaticCredentialsProvider::create)
            .orElseGet(DefaultCredentialsProvider::create);

    return S3Client.builder()
        .credentialsProvider(provider)
        .region(Region.of(reportStorageClientProperties.getRegion()))
        .endpointOverride(URI.create(reportStorageClientProperties.getUrl()))
        .serviceConfiguration(
            S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build())
        .overrideConfiguration(
            ClientOverrideConfiguration
                .builder()
                .putAdvancedOption(SdkAdvancedClientOption.SIGNER, AwsS3V4Signer.create())
                .build())
        .build();
  }

  @Bean
  S3CompatibleStorage reportStorageService(
      S3Client s3Client,
      ReportStorageRequestStatusCheck reportStorageChecker,
      @Valid ReportStorageProperties properties) {

    return new S3CompatibleStorage(
        s3Client, reportStorageChecker, properties.getDefaultBucket(), properties.getSse());
  }

  @Bean
  ReportStorageRequestStatusCheck reportStorageChecker(
      S3Client s3Client,
      ReportPersistenceService reportPersistenceService) {

    return new ReportStorageRequestStatusCheck(s3Client, reportPersistenceService);
  }
}
