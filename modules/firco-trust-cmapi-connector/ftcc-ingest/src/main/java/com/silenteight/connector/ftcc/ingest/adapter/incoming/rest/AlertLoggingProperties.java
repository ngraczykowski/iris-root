package com.silenteight.connector.ftcc.ingest.adapter.incoming.rest;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "ftcc.alert.logging")
class AlertLoggingProperties {

  private boolean active;
}
