/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.scb-bridge.gns-rt")
public record GnsRtResponseMapperConfigurationProperties(boolean attachQcoFieldsToResponse) {}
