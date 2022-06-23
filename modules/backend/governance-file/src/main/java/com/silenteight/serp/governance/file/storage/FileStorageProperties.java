package com.silenteight.serp.governance.file.storage;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties("serp.governance.files")
class FileStorageProperties {

  @NotNull
  String defaultBucket;
}
