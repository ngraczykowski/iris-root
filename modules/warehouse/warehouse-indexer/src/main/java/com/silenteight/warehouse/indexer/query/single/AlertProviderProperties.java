package com.silenteight.warehouse.indexer.query.single;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.alert-provider")
public class AlertProviderProperties {

  boolean isSqlSupportEnabled;
}
