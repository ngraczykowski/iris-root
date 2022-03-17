package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.Data;

import com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("serp.scb.bridge.ingest")
@Component
@Validated
@Data
class IngestProperties {

  @NotBlank
  private String outputExchange = MessagingConstants.EXCHANGE_ALERT_UNPROCESSED;

  @NotBlank
  private String reportOutputExchange = MessagingConstants.EXCHANGE_REPORT_DATA;

  private boolean solvedAlertsProcessingEnabled;
}
