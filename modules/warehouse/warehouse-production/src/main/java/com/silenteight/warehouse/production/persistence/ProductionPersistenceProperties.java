package com.silenteight.warehouse.production.persistence;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer")
public class ProductionPersistenceProperties {

  @NotNull
  private Integer productionBatchSize;
}
