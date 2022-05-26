package com.silenteight.scb.ingest.adapter.incomming.common.rest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ GlobalHealthEndpointProperties.class })
public class GlobalHealthEndpointConfiguration {
}
