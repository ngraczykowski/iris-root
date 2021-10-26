package com.silenteight.payments.bridge.agents.service;

import lombok.Setter;

import com.silenteight.proto.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgentGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CompanyNameSurroundingAgentConfiguration {

  @Setter(onMethod_ = @GrpcClient("companynamesurroundingagent"))
  private Channel companyNameSurroundingAgentChannel;

  @Bean
  CompanyNameSurroundingAgent companyNameSurroundingAgent() {
    var stub = CompanyNameSurroundingAgentGrpc
        .newBlockingStub(companyNameSurroundingAgentChannel)
        .withWaitForReady();

    return new CompanyNameSurroundingAgent(stub);
  }
}
