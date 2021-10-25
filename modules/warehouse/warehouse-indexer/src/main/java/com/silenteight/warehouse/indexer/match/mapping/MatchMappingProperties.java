package com.silenteight.warehouse.indexer.match.mapping;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.match-mapping")
public class MatchMappingProperties {

  List<String> ignoredKeys = List.of();
}
