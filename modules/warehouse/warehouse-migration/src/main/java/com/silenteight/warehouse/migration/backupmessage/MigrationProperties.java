package com.silenteight.warehouse.migration.backupmessage;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.alert.migration")
class MigrationProperties {

  int batchSize;
}
