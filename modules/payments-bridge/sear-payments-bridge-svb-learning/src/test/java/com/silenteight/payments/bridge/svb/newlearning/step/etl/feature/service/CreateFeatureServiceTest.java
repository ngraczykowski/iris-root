package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.learning.features.port.outgoing.CreateAgentInputsClient;
import com.silenteight.payments.bridge.svb.newlearning.job.HistoricalRiskAssessmentFeatureUseCaseMock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createEtlHit;
import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createHitComposite;
import static com.silenteight.payments.bridge.svb.newlearning.step.etl.EtlFixture.createRegisterAlert;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFeatureServiceTest {

  private CreateFeatureService createFeatureService;

  @Mock
  private CreateAgentInputsClient createAgentInputsClient;

  @Mock
  private CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @BeforeEach
  void setUp() {
    var historicalRiskAssessmentFeature = new HistoricalRiskAssessmentFeatureUseCaseMock();
    createFeatureService = new CreateFeatureService(
        List.of(
            new GeoFeatureExtractorService(),
            new IdentificationMismatchExtractor(),
            new NameFeatureExtractorService(createNameFeatureInputUseCase),
            new OrganizationNameAgentExtractorService(),
            new HistoricalRiskAccountNumberExtractor(historicalRiskAssessmentFeature),
            new HistoricalRiskCustomerNameExtractor(historicalRiskAssessmentFeature)),
        List.of(new NameMatchedTextFeatureExtractorService(createNameFeatureInputUseCase)),
        createAgentInputsClient);

    when(createNameFeatureInputUseCase.create(any())).thenReturn(
        NameFeatureInput.newBuilder().build());
  }

  @Test
  void shouldExtractFeatures() {
    var hit = createEtlHit();
    var agentInput = createFeatureService.createFeatureInputs(List.of(hit), createRegisterAlert());
    assertThat(agentInput.get(0).getFeatureInputsCount()).isEqualTo(6);

    var unstructuredAgentInput =
        createFeatureService.createUnstructuredFeatureInputs(
            List.of(createHitComposite()), createRegisterAlert());
    assertThat(unstructuredAgentInput.get(0).getFeatureInputsCount()).isEqualTo(1);
  }
}

