package com.silenteight.customerbridge.common.ingest;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_ALERT_UNPROCESSED;
import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_REPORT_DATA;

@ConfigurationProperties("serp.scb.bridge.ingest")
@Component
@Validated
@Data
class IngestProperties {

  @NotBlank
  private String outputExchange = EXCHANGE_ALERT_UNPROCESSED;

  @NotBlank
  private String reportOutputExchange = EXCHANGE_REPORT_DATA;

  private boolean solvedAlertsProcessingEnabled;
}
