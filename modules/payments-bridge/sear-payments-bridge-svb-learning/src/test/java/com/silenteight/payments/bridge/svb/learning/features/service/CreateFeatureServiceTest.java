package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.silenteight.payments.bridge.svb.learning.LearningAlertFixture.createLearningAlert;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFeatureServiceTest {

  private CreateFeaturesService createFeaturesService;
  @Mock
  private CreateAgentInputsClient createAgentInputsClient;
  @Mock
  private CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @BeforeEach
  void setUp() {
    createFeaturesService = new CreateFeaturesService(
        createAgentInputsClient,
        List.of(
            new GeoFeatureExtractor(),
            new NameFeatureExtractor(createNameFeatureInputUseCase),
            new GeoFeatureExtractor()));
    when(createNameFeatureInputUseCase.createDefault(any())).thenReturn(
        NameFeatureInput.newBuilder().build());
  }

  @Test
  void shouldExtractAllFeatures() {
    var resp = createFeaturesService.createMatchFeatures(createLearningAlert());
    assertThat(resp.size()).isEqualTo(4);
    assertThat(resp.get(0).getFeatureInputsList().size()).isEqualTo(3);
  }

  @Test
  void shouldSendFeatures() {
    createFeaturesService.createMatchFeatures(createLearningAlert());
    verify(createAgentInputsClient).createAgentInputs(any());
  }
}
