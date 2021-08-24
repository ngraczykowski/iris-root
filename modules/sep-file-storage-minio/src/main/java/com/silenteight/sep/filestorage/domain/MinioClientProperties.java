package com.silenteight.sep.filestorage.domain;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "warehouse.minio")
class MinioClientProperties {

  @NotBlank
  String url;

  @NotBlank
  String accessKey;

  @NotBlank
  String privateKey;
}
