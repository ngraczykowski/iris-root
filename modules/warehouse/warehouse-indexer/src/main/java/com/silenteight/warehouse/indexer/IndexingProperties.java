package com.silenteight.warehouse.indexer;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexing")
public class IndexingProperties {

  @NotBlank
  private String environmentPrefix;
}
