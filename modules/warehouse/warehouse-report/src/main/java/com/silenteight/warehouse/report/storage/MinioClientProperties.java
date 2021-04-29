package com.silenteight.warehouse.report.storage;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.minio")
class MinioClientProperties {

  @NotBlank
  String url;

  @NotBlank
  String accessKey;

  @NotBlank
  String privateKey;
}
