package com.silenteight.warehouse.indexer.alert;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.es")
public class ElasticsearchProperties {

  @NotBlank
  String productionQueryIndex;

  @NotNull
  List<String> countryRolesIndexes = List.of();

  int updateRequestBatchSize;
}
