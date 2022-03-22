package com.silenteight.payments.bridge.svb.learning.job;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc;
import com.silenteight.datasource.categories.api.v2.CategoryServiceGrpc;
import com.silenteight.payments.bridge.ae.alertregistration.port.FindRegisteredAlertUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;
import com.silenteight.payments.bridge.agents.port.*;
import com.silenteight.payments.bridge.data.retention.port.CreateAlertDataRetentionUseCase;
import com.silenteight.payments.bridge.data.retention.port.CreateFileRetentionUseCase;
import com.silenteight.payments.bridge.datasource.agent.CreateFeatureInputsProcess;
import com.silenteight.payments.bridge.datasource.agent.FeatureInputRepository;
import com.silenteight.payments.bridge.datasource.agent.infrastructure.CreateAgentInputsClient;
import com.silenteight.payments.bridge.datasource.category.CategoryValueRepository;
import com.silenteight.payments.bridge.datasource.category.CreateCategoryValuesProcess;
import com.silenteight.payments.bridge.datasource.category.infrastructure.CategoriesClient;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.svb.learning.port.HistoricalDecisionLearningEnginePort;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.warehouse.index.port.IndexLearningUseCase;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@EnableConfigurationProperties(DatasourceClientProperties.class)
public class TestApplicationConfiguration {

  @Rule
  public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private final DatasourceClientProperties properties;

  @Bean
  BatchProperties batchProperties() {
    BatchProperties properties = new BatchProperties();
    properties.setTablePrefix("pb_batch_");
    return properties;
  }

  @Bean
  CreateFileRetentionUseCase createFileRetentionUseCase() {
    return new CreateFileRetentionUseCaseMock();
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

  @Bean
  CreateContextualLearningFeatureInputUseCase createContextualLearningFeatureInputUseCase() {
    return new CreateContextualLearningFeatureInputUseCaseMock();
  }

  @Bean
  CategoryValueRepository categoryValueRepository() {
    return new RemoteDatasourceCategoryValueRepositoryMock();
  }

  @Bean
  CreateCategoryValuesProcess createCategoryValuesProcess(
      final CategoryValueRepository categoryValueRepository) {
    return new CreateCategoryValuesProcess(Collections.emptyList(), Collections.emptyList(),
        categoryValueRepository);
  }

  @Bean
  CategoriesClient categoriesClient() {
    var stub = CategoryServiceGrpc
        .newBlockingStub(getManagedChannel())
        .withWaitForReady();

    return new CategoriesClient(stub, properties.getTimeout());
  }

  @Bean
  FeatureInputRepository featureInputRepository() {
    return new RemoteDatasourceFeatureInputRepositoryMock();
  }

  @Bean
  CreateFeatureInputsProcess createFeatureInputsProcess(
      final FeatureInputRepository featureInputRepository) {
    return new CreateFeatureInputsProcess(Collections.emptyList(), Collections.emptyList(),
        featureInputRepository);
  }

  @Bean
  CreateAgentInputsClient createAgentInputsClient() {
    var stub = AgentInputServiceGrpc
        .newStub(getManagedChannel())
        .withWaitForReady();

    return new CreateAgentInputsClient(stub, properties.getTimeout());
  }

  private ManagedChannel getManagedChannel() {
    var serverName = InProcessServerBuilder.generateName();
    return grpcCleanup.register(
        InProcessChannelBuilder.forName(serverName).directExecutor().build());
  }
}
