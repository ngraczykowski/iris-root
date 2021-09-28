package com.silenteight.warehouse.indexer.query.streaming;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.es.scroll.search")
class ScrollSearchProperties {

  @NotNull
  private Integer batchSize;
  @NotNull
  private Integer keepAliveSeconds;
}
