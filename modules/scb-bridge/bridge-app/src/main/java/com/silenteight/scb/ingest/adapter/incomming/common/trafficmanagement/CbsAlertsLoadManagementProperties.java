package com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.traffic-manager.load-management")
record CbsAlertsLoadManagementProperties(boolean enabled,
                                         long maxLoadThreshold,
                                         long fairLoadThreshold) {}
