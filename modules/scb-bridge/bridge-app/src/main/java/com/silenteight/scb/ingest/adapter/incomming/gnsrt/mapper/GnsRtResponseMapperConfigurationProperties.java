package com.silenteight.scb.ingest.adapter.incomming.gnsrt.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.gns-rt")
public record GnsRtResponseMapperConfigurationProperties(boolean attachQcoFieldsToResponse) {}
