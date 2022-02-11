package com.silenteight.bridge.core.registration.infrastructure.grpc;

import com.silenteight.adjudication.api.library.v1.alert.AlertGrpcAdapter;
import com.silenteight.adjudication.api.library.v1.alert.AlertServiceClient;
import com.silenteight.adjudication.api.library.v1.analysis.AnalysisGrpcAdapter;
import com.silenteight.adjudication.api.library.v1.analysis.AnalysisServiceClient;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.bridge.core.recommendation.infrastructure.amqp.RecommendationIncomingRecommendationsGeneratedConfigurationProperties;
import com.silenteight.bridge.core.registration.infrastructure.RegistrationAnalysisProperties;
import com.silenteight.governance.api.library.v1.model.ModelGrpcAdapter;
import com.silenteight.governance.api.library.v1.model.ModelServiceClient;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties({ GrpcConfigurationProperties.class })
public class GrpcServiceConfiguration {

  @GrpcClient(RegistrationKnownServices.GOVERNANCE)
  SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;

  @GrpcClient(RegistrationKnownServices.ADJUDICATION_ENGINE)
  AnalysisServiceBlockingStub analysisServiceBlockingStub;

  @GrpcClient(RegistrationKnownServices.ADJUDICATION_ENGINE)
  AlertServiceBlockingStub alertServiceBlockingStub;

  @Bean
  @Profile({ "dev", "test" })
  ModelServiceClient modelServiceClientMock() {
    return new ModelServiceClientMock();
  }

  @Bean
  @Profile({ "dev", "test" })
  AnalysisServiceClient analysisServiceClientMock(
      RabbitTemplate rabbitTemplate,
      RecommendationIncomingRecommendationsGeneratedConfigurationProperties properties,
      RegistrationAnalysisProperties registrationAnalysisProperties) {
    return new AnalysisServiceClientMock(
        rabbitTemplate, properties, registrationAnalysisProperties);
  }

  @Bean
  @Profile({ "dev", "test" })
  AlertServiceClient alertServiceClientMock() {
    return new AlertServiceClientMock();
  }

  @Bean
  @ConditionalOnMissingBean
  ModelServiceClient modelServiceClient(GrpcConfigurationProperties properties) {
    return new ModelGrpcAdapter(
        solvingModelServiceBlockingStub,
        properties.governanceDeadline().getSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  AnalysisServiceClient analysisServiceClient(GrpcConfigurationProperties properties) {
    return new AnalysisGrpcAdapter(
        analysisServiceBlockingStub,
        properties.adjudicationEngineDeadline().getSeconds());
  }

  @Bean
  @ConditionalOnMissingBean
  AlertServiceClient alertServiceClient(GrpcConfigurationProperties properties) {
    return new AlertGrpcAdapter(
        alertServiceBlockingStub,
        properties.adjudicationEngineDeadline().getSeconds());
  }
}
