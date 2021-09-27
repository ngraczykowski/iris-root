package com.silenteight.sep.filestorage.minio;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "sep.filestorage.minio")
class MinioClientProperties {

  @NotBlank
  String url;

  @NotBlank
  String accessKey;

  @NotBlank
  String privateKey;

  @NotBlank
  String region;
}
