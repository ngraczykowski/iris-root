package com.silenteight.bridge.core.registration.infrastructure.grpc;

import com.silenteight.bridge.core.registration.infrastructure.util.KnownServices;
import com.silenteight.governance.api.library.v1.model.ModelGrpcAdapter;
import com.silenteight.governance.api.library.v1.model.ModelServiceClient;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties({ GrpcConfigurationProperties.class })
public class GrpcServiceConfiguration {

  @GrpcClient(KnownServices.GOVERNANCE_CONNECTOR)
  SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;

  @Bean
  @Profile({ "dev", "test" })
  ModelServiceClient modelServiceClientMock() {
    return new ModelServiceClientMock();
  }

  @Bean
  @ConditionalOnMissingBean
  ModelServiceClient modelServiceClient(GrpcConfigurationProperties properties) {
    return new ModelGrpcAdapter(
        solvingModelServiceBlockingStub,
        properties.governanceDeadline().getSeconds());
  }
}
