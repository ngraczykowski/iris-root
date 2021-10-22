package com.silenteight.payments.bridge.firco.datasource.service.process.category;

import lombok.Setter;

import com.silenteight.proto.agent.organizationname.v1.api.OrganizationNameAgentGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CategoryValueProcessConfiguration {

  @Setter(onMethod_ = @GrpcClient("organization-name-agent"))
  private Channel organizationNameAgentChannel;

  @Bean
  CategoryValueProcess organizationNameProcess() {
    var stub = OrganizationNameAgentGrpc
        .newBlockingStub(organizationNameAgentChannel)
        .withWaitForReady();

    return new OrganizationNameProcess(stub);
  }

}
