package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;
import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AgentEtlProperties.class)
class AgentEtlConfiguration {

  @Valid
  private final AgentEtlProperties properties;

  @Setter(onMethod_ = @GrpcClient("datasource"))
  private Channel dataSourceChannel;

  @Bean
  NameAgentEtlProcess nameAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new NameAgentEtlProcess(stub, properties.getTimeout());
  }

  @Bean
  GeoAgentEtlProcess geoAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new GeoAgentEtlProcess(stub, properties.getTimeout());
  }

  @Bean
  OrganizationNameAgentEtlProcess organizationNameAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new OrganizationNameAgentEtlProcess(stub, properties.getTimeout());
  }

  @Bean
  IdentificationMismatchAgentEtlProcess identificationMismatchAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new IdentificationMismatchAgentEtlProcess(stub, properties.getTimeout());
  }

  @Bean
  NameMatchedTextAgentEtlProcess nameMatchedTextAgentEtlProcess() {
    var stub = AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();

    return new NameMatchedTextAgentEtlProcess(stub, properties.getTimeout());
  }

  @Bean
  HistoricalRiskAssessmentAgentEtlProcess historicalRiskAssessmentAgentEtlProcess(
      HistoricalRiskCustomerNameFeature historicalRiskCustomerNameFeature,
      HistoricalRiskAccountNumberFeature historicalRiskAccountNumberFeature,
      HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureAgent) {
    var stub = getStub();

    return new HistoricalRiskAssessmentAgentEtlProcess(
        stub, properties.getTimeout(),
        List.of(historicalRiskCustomerNameFeature, historicalRiskAccountNumberFeature),
        historicalRiskAssessmentFeatureAgent);
  }

  private AgentInputServiceBlockingStub getStub() {
    return AgentInputServiceGrpc
        .newBlockingStub(dataSourceChannel)
        .withWaitForReady();
  }
}
