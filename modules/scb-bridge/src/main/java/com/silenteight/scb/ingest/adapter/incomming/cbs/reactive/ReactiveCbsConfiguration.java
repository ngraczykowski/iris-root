package com.silenteight.scb.ingest.adapter.incomming.cbs.reactive;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckGateway;

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
