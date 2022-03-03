package com.silenteight.customerbridge.cbs.reactive;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.gateway.CbsAckGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class ReactiveCbsConfiguration {

  private final CbsAckGateway cbsAckGateway;

  @Bean
  ReactiveCbsAckService reactiveCbsAckService() {
    return new ReactiveCbsAckService(cbsAckGateway);
  }
}
