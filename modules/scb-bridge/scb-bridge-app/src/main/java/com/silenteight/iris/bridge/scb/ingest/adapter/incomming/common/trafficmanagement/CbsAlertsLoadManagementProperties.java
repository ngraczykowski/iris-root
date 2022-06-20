/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.trafficmanagement;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.traffic-manager.load-management")
record CbsAlertsLoadManagementProperties(boolean enabled,
                                         long maxLoadThreshold,
                                         long fairLoadThreshold) {}
