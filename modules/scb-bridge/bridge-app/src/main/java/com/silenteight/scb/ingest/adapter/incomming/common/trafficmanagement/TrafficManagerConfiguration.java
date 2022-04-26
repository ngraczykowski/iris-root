package com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    GnsRtSemaphoreProperties.class, CbsAlertsLoadManagementProperties.class })
class TrafficManagerConfiguration {
}
