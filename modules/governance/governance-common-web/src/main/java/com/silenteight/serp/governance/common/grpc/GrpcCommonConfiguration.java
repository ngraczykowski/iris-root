package com.silenteight.serp.governance.common.grpc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(GrpcCommonProperties.class)
class GrpcCommonConfiguration {
}
