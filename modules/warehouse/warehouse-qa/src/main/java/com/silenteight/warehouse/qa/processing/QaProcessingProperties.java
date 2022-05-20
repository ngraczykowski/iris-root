package com.silenteight.warehouse.qa.processing;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
public class QaProcessingProperties {

  @NotNull
  private Integer qaBatchSize;
}
