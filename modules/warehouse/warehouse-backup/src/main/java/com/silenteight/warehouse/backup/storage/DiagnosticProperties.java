package com.silenteight.warehouse.backup.storage;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.indexer.diagnostic")
class DiagnosticProperties {

  boolean enabled = false;
}
