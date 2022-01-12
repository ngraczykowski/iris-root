package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.payments.bridge.svb.learning.features.port.outgoing.CreateAgentInputsClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service.FeatureFixture.createEtlHit;
import static com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service.FeatureFixture.createRegisterAlert;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class CreateFeatureServiceTest {

  private CreateFeatureService createFeatureService;
  @Mock
  private CreateAgentInputsClient createAgentInputsClient;

  @BeforeEach
  void setUp() {
    createFeatureService = new CreateFeatureService(
        List.of(
            new GeoFeatureExtractorService(),
            new IdentificationMismatchExtractor(),
            new NameFeatureExtractorService(),
            new NameMatchedTextFeatureExtractorService(),
            new OrganizationNameAgentExtractorService()),
        createAgentInputsClient);
  }

  @Test
  void shouldExtractFeatures() {
    var hit = createEtlHit();
    var agentInput = createFeatureService.createFeatureInputs(List.of(hit), createRegisterAlert());
    assertThat(agentInput.get(0).getFeatureInputsCount()).isEqualTo(5);
  }
}

