package com.silenteight.warehouse.report.storage;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import software.amazon.awssdk.services.s3.model.ServerSideEncryption;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.report")
class ReportStorageProperties {

  @NotBlank
  private String defaultBucket;

  @Nullable
  private ServerSideEncryption sse;
}
