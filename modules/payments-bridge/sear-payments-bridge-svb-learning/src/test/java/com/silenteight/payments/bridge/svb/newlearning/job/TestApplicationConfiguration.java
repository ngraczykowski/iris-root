package com.silenteight.payments.bridge.svb.newlearning.job;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.agents.port.*;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.svb.learning.engine.HistoricalDecisionLearningEnginePort;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class TestApplicationConfiguration {

  @Bean
  BatchProperties batchProperties() {
    BatchProperties properties = new BatchProperties();
    properties.setTablePrefix("pb_batch_");
    return properties;
  }

  @Bean
  CompanyNameSurroundingUseCase companyNameSurroundingUseCase() {
    return new CompanyNameSurroundingUseCaseMock();
  }

  @Bean
  HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureUseCase() {
    return new HistoricalRiskAssessmentFeatureUseCaseMock();
  }

  @Bean
  HistoricalRiskAssessmentUseCase historicalRiskAssessmentUseCase() {
    return new HistoricalRiskAssessmentUseCaseMock();
  }

  @Bean
  SpecificTerms2UseCase specificTerms2UseCase() {
    return new SpecificTerms2UseCaseMock();
  }

  @Bean
  SpecificTermsUseCase specificTermsUseCase() {
    return new SpecificTermsUseCaseMock();
  }

  @Bean
  CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase() {
    return new CreateAlertedPartyEntitiesUseCaseMock();
  }

  @Bean
  NameAddressCrossmatchUseCase nameAddressCrossmatchUseCase() {
    return new NameAddressCrossmatchUseCaseMock();
  }

  @Bean
  CreateCategoryValuesClient createCategoryValuesClient() {
    return new CreateCategoriesValuesMock();
  }

  @Bean
  CreateAgentInputsClient createAgentInputsClient() {
    return new CreateAgentInputsClientMock();
  }

  @Bean
  FindRegisteredAlertUseCase findRegisteredAlertPort() {
    return new FindRegisteredAlertPortMock();
  }

  @Bean
  RegisterAlertUseCase registerAlertUseCase() {
    return new RegisterAlertUseCaseMock();
  }

  @Bean
  MessageParserUseCase messageParserUseCase() {
    return new MessageParserMock();
  }

  @Bean
  HistoricalDecisionLearningEnginePort historicalDecisionLearningEnginePort() {
    return new HistoricalDecisionLearningEngineMock();
  }

  @Bean
  CreateAlertDataRetentionUseCase createAlertDataRetentionUseCase() {
    return alerts -> {

    };
  }

  @Bean
  IndexLearningUseCase indexLearningUseCase() {
    return new IndexLearningUseCaseMock();
  }

  @Bean
  CreateNameFeatureInputUseCase createNameFeatureInputUseCase() {
    return new CreateNameFeatureInputUseCaseMock();
  }
}
