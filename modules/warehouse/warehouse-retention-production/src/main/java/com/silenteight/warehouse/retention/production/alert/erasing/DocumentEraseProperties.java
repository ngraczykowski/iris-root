package com.silenteight.warehouse.retention.production.alert.erasing;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "warehouse.es")
class DocumentEraseProperties {

  int eraseRequestBatchSize;
}
