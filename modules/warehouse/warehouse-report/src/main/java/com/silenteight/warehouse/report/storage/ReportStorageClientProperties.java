package com.silenteight.warehouse.report.storage;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties(prefix = "sep.filestorage.minio")
@Validated
class ReportStorageClientProperties {

  @NotBlank
  private String url;

  @NotBlank
  private String accessKey;

  @NotBlank
  private String privateKey;

  @NotBlank
  private String region;
}
