package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("serp.scb.bridge.alert.reader")
@Component
@Data
@Validated
class AlertReaderProperties {

  private boolean solvedAlertsProcessingEnabled;
}
