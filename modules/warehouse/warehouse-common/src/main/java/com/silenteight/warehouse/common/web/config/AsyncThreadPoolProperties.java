package com.silenteight.warehouse.common.web.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.task.async.execution.pool")
class AsyncThreadPoolProperties {

  @NotNull
  private Integer coreSize;
  @NotNull
  private Integer maxSize;
  @NotNull
  private Integer queueCapacity;
  @NotBlank
  private String threadNamePrefix;
}
