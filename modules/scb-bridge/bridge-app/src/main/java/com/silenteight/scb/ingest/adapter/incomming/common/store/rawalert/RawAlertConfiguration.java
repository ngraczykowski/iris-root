package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ RawAlertsDataRetentionProperties.class })
class RawAlertConfiguration {
}
