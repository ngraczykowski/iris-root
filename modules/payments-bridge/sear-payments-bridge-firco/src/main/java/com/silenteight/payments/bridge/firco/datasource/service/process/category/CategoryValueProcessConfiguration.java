package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.Setter;

import com.silenteight.proto.agent.companynamesurrounding.v1.api.CompanyNameSurroundingAgentGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;

public class CategoryValueProcessConfiguration {

  @Setter(onMethod_ = @GrpcClient("companynamesurroundingagent"))
  private Channel companyNameSurroundingAgentChannel;

  @Bean
  CompanyNameSurroundingProcess companyNameSurroundingProcess() {
    var stub = CompanyNameSurroundingAgentGrpc
        .newBlockingStub(companyNameSurroundingAgentChannel)
        .withWaitForReady();

    return new CompanyNameSurroundingProcess(stub);
  }


}
