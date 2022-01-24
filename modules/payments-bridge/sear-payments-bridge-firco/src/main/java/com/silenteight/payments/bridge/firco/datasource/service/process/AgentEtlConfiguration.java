package com.silenteight.payments.bridge.firco.datasource.service.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.datasource.port.CreateAgentInputsClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
class AgentEtlConfiguration {

  private final CreateAgentInputsClient createAgentInputsClient;
  
  @Bean
  NameAgentEtlProcess nameAgentEtlProcess(
      CreateNameFeatureInputUseCase createNameFeatureInputUseCase) {
    return new NameAgentEtlProcess(createAgentInputsClient, createNameFeatureInputUseCase);
  }

  @Bean
  GeoAgentEtlProcess geoAgentEtlProcess() {
    return new GeoAgentEtlProcess(createAgentInputsClient);
  }

  @Bean
  OrganizationNameAgentEtlProcess organizationNameAgentEtlProcess() {
    return new OrganizationNameAgentEtlProcess(createAgentInputsClient);
  }

  @Bean
  IdentificationMismatchAgentEtlProcess identificationMismatchAgentEtlProcess() {
    return new IdentificationMismatchAgentEtlProcess(createAgentInputsClient);
  }

  @Bean
  NameMatchedTextAgentEtlProcess nameMatchedTextAgentEtlProcess(
      CreateNameFeatureInputUseCase createNameFeatureInputUseCase) {
    return new NameMatchedTextAgentEtlProcess(
        createAgentInputsClient, createNameFeatureInputUseCase);
  }

  @Bean
  HistoricalRiskAssessmentAgentEtlProcess historicalRiskAssessmentAgentEtlProcess(
      HistoricalRiskCustomerNameFeature historicalRiskCustomerNameFeature,
      HistoricalRiskAccountNumberFeature historicalRiskAccountNumberFeature,
      HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureAgent) {
    return new HistoricalRiskAssessmentAgentEtlProcess(
        createAgentInputsClient,
        List.of(historicalRiskCustomerNameFeature, historicalRiskAccountNumberFeature),
        historicalRiskAssessmentFeatureAgent);
  }
}
