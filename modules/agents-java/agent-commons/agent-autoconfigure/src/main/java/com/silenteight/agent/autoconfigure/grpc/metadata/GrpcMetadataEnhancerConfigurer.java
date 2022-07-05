package com.silenteight.agent.autoconfigure.grpc.metadata;

import lombok.RequiredArgsConstructor;

import com.silenteight.agent.autoconfigure.grpc.metadata.AgentInformationEnhancingServerInterceptor.EnhancingServerInterceptorConfiguration;

import io.grpc.ServerBuilder;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;

@RequiredArgsConstructor
public class GrpcMetadataEnhancerConfigurer {

  private final AgentInformationProvider agentInformationProvider;
  private final String agentNameField;
  private final String agentVersionField;

  public GrpcServerConfigurer getGrpcServerConfigurer() {
    return this::addInterceptor;
  }

  private void addInterceptor(ServerBuilder<?> serverBuilder) {
    var fieldsConfiguration = new EnhancingServerInterceptorConfiguration(
        agentNameField, agentVersionField);

    serverBuilder.intercept(new AgentInformationEnhancingServerInterceptor(
        AgentInformation.fromProvider(agentInformationProvider), fieldsConfiguration)
    );
  }
}
