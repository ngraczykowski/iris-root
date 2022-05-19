package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.retention.raw-alerts")
record RawAlertsDataRetentionProperties(Duration expiredAfter) {
}
