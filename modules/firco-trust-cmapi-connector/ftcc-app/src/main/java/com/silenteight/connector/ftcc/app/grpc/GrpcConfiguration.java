package com.silenteight.connector.ftcc.app.grpc;

import com.silenteight.proto.recommendation.api.v1.RecommendationServiceGrpc.RecommendationServiceBlockingStub;
import com.silenteight.proto.registration.api.v1.RegistrationServiceGrpc.RegistrationServiceBlockingStub;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceClient;
import com.silenteight.recommendation.api.library.v1.RecommendationServiceGrpcAdapter;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;
import com.silenteight.registration.api.library.v1.RegistrationServiceGrpcAdapter;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfiguration {

  private static final long DEADLINE_IN_SECONDS = 30L;

  @GrpcClient("core-bridge")
  RegistrationServiceBlockingStub registrationServiceBlockingStub;

  @GrpcClient("core-bridge")
  RecommendationServiceBlockingStub recommendationServiceBlockingStub;

  @Bean
  RegistrationServiceClient registrationServiceClient() {
    return new RegistrationServiceGrpcAdapter(registrationServiceBlockingStub, DEADLINE_IN_SECONDS);
  }

  @Bean
  RecommendationServiceClient reregistrationServiceClient() {
    return new RecommendationServiceGrpcAdapter(
        recommendationServiceBlockingStub, DEADLINE_IN_SECONDS);
  }
}
